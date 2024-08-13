package com.amorgakco.backend.jwt.service;

import com.amorgakco.backend.global.exception.*;
import com.amorgakco.backend.global.exception.IllegalAccessException;
import com.amorgakco.backend.jwt.domain.RefreshToken;
import com.amorgakco.backend.jwt.dto.MemberJwt;
import com.amorgakco.backend.jwt.repository.RefreshTokenRepository;

import jakarta.servlet.http.Cookie;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;
    private final JwtValidator jwtValidator;
    private final JwtCreator jwtCreator;
    private final RefreshTokenRepository refreshTokenRepository;

    public MemberJwt reissue(final String refreshToken) {
        final RefreshToken savedRefreshToken = findRefreshTokenFromRedis(refreshToken);
        final String savedMemberId = savedRefreshToken.getMemberId();
        refreshTokenRepository.delete(savedRefreshToken);
        return createAndSaveMemberToken(savedMemberId);
    }

    private RefreshToken findRefreshTokenFromRedis(final String token) {
        return refreshTokenRepository
                .findById(token)
                .orElseThrow(ResourceNotFoundException::refreshTokenNotFound);
    }

    public MemberJwt createAndSaveMemberToken(final String memberId) {
        final String accessToken = jwtCreator.create(memberId, jwtProperties.accessExpiration());
        final String refreshToken = jwtCreator.create(memberId, jwtProperties.refreshExpiration());
        refreshTokenRepository.save(new RefreshToken(refreshToken, memberId));
        return new MemberJwt(accessToken, refreshToken);
    }

    public void logout(final Optional<Cookie> cookie) {
        final Cookie tokenCookie = cookie.orElseThrow(IllegalAccessException::refreshTokenRequired);
        final String refreshToken = tokenCookie.getValue();
        refreshTokenRepository.deleteById(refreshToken);
    }
}
