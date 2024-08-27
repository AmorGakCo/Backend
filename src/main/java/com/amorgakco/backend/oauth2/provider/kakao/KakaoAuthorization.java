package com.amorgakco.backend.oauth2.provider.kakao;


public record KakaoAuthorization(
        String tokenType,
        String accessToken,
        String idToken,
        Integer expiresIn,
        String refreshToken,
        Integer refreshTokenExpiresIn,
        String scope) {}
