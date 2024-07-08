package com.amorgakco.backend.jwt.service;

import com.amorgakco.backend.jwt.domain.RefreshToken;
import com.amorgakco.backend.jwt.dto.MemberJwt;
import com.amorgakco.backend.jwt.exception.AccessTokenRequiredException;
import com.amorgakco.backend.jwt.exception.InvalidJwtException;
import com.amorgakco.backend.jwt.exception.RefreshTokenRequiredException;
import com.amorgakco.backend.jwt.repository.RefreshTokenRepository;
import com.amorgakco.backend.member.service.MemberService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.http.Cookie;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
@Transactional
public class JwtService {
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final int TOKEN_PREFIX_LENGTH = 7;
    private final JwtProperties jwtProperties;
    private final MemberService memberService;
    private final SecretKey secretKey;
    private final JwtValidator jwtValidator;
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public JwtService(
            final JwtProperties jwtProperties,
            final MemberService memberService,
            final JwtValidator jwtValidator,
            final RefreshTokenRepository refreshTokenRepository) {
        this.jwtProperties = jwtProperties;
        this.secretKey =
                Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes(StandardCharsets.UTF_8));
        this.memberService = memberService;
        this.jwtValidator = jwtValidator;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /** Refresh Token Rotation */
    public MemberJwt reissue(final Optional<Cookie> cookie, final String accessTokenHeader) {
        final Cookie tokenCookie = cookie.orElseThrow(RefreshTokenRequiredException::new);
        final String oldRefreshToken = tokenCookie.getValue();
        final String accessToken =
                extractAccessToken(accessTokenHeader)
                        .orElseThrow(AccessTokenRequiredException::new);
        if (jwtValidator.reissueValidate(oldRefreshToken, accessToken)) {
            final RefreshToken refreshToken =
                    refreshTokenRepository
                            .findById(oldRefreshToken)
                            .orElseThrow(InvalidJwtException::new);
            final String memberId = refreshToken.getMemberId();
            refreshTokenRepository.delete(refreshToken);
            return createAndSaveMemberToken(memberId);
        }
        throw new InvalidJwtException();
    }

    public Optional<String> extractAccessToken(final String accessTokenWithBearer) {
        if (StringUtils.hasText(accessTokenWithBearer)
                && accessTokenWithBearer.startsWith(TOKEN_PREFIX)) {
            return Optional.of(accessTokenWithBearer.substring(TOKEN_PREFIX_LENGTH));
        }
        return Optional.empty();
    }

    public MemberJwt createAndSaveMemberToken(final String memberId) {
        final String accessToken = create(memberId, jwtProperties.accessExpiration());
        final String refreshToken = create(memberId, jwtProperties.refreshExpiration());
        refreshTokenRepository.save(new RefreshToken(refreshToken, memberId));
        return new MemberJwt(accessToken, refreshToken);
    }

    public String create(final String memberId, final Long duration) {
        final Date now = new Date();
        final Date expirationDate = new Date(now.getTime() + duration);
        return Jwts.builder()
                .subject(memberId)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    public void logout(final Optional<Cookie> cookie) {
        final Cookie tokenCookie = cookie.orElseThrow(RefreshTokenRequiredException::new);
        final String refreshToken = tokenCookie.getValue();
        refreshTokenRepository.deleteById(refreshToken);
    }
}
