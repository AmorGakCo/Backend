package com.amorgakco.backend.jwt.service;

import com.amorgakco.backend.jwt.exception.AccessTokenExpiredException;
import com.amorgakco.backend.jwt.exception.InvalidJwtException;
import com.amorgakco.backend.jwt.exception.RefreshTokenExpiredException;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.service.MemberService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class JwtValidator {
    private static final String EMPTY_CREDENTIAL = "";
    private final MemberService memberService;
    private final SecretKey secretKey;

    @Autowired
    public JwtValidator(final JwtProperties jwtProperties, final MemberService memberService) {
        this.secretKey =
                Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes(StandardCharsets.UTF_8));
        this.memberService = memberService;
    }

    public Authentication getAuthentication(final String token) {
        checkAccessToken(token);
        final Member member = memberService.getMember(Long.parseLong(getClaim(token)));
        return getUsernamePasswordAuthenticationToken(member);
    }

    public void checkAccessToken(final String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        } catch (final ExpiredJwtException e) {
            throw new AccessTokenExpiredException();
        } catch (final JwtException e) {
            throw new InvalidJwtException();
        }
    }

    public String getClaim(final String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(
            final Member member) {
        return new UsernamePasswordAuthenticationToken(
                member, EMPTY_CREDENTIAL, getAuthorityList(member));
    }

    private List<GrantedAuthority> getAuthorityList(final Member m) {
        return AuthorityUtils.createAuthorityList(
                m.getRoles().stream().map(r -> r.getRole().toString()).toList());
    }

    public boolean reissueValidate(final String refreshToken, final String accessToken) {
        checkRefreshToken(refreshToken);
        try {
            checkAccessToken(accessToken);
        } catch (final AccessTokenExpiredException e) {
            return true;
        }
        return false;
    }

    public void checkRefreshToken(final String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        } catch (final ExpiredJwtException e) {
            throw new RefreshTokenExpiredException();
        } catch (final JwtException e) {
            throw new InvalidJwtException();
        }
    }
}
