package com.amorgakco.backend.jwt.controller;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class JwtCookieLoader {

    private static final int COOKIE_AGE_SECONDS = 604800;
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh-token";

    public void loadCookie(final HttpServletResponse response, final String refreshToken) {
        final ResponseCookie refreshTokenCookie = makeCookie(refreshToken);
        response.setHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    private ResponseCookie makeCookie(final String token) {
        return ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, token)
                .maxAge(COOKIE_AGE_SECONDS)
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();
    }
}
