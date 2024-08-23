package com.amorgakco.backend.jwt.controller;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class JwtCookieLoader {

    private static final int REFRESH_COOKIE_AGE_SECONDS = 604800;
    private static final int ACCESS_COOKIE_AGE_SECONDS = 180;
    private static final String REFRESH_COOKIE_NAME = "refresh-token";
    private static final String ACCESS_COOKIE_NAME = "access-token";

    public void loadCookies(
            final HttpServletResponse response,
            final String refreshToken,
            final String accessToken) {
        final ResponseCookie refreshTokenCookie =
                makeCookie(REFRESH_COOKIE_NAME, REFRESH_COOKIE_AGE_SECONDS, refreshToken);
        final ResponseCookie accessTokenCookie =
                makeCookie(ACCESS_COOKIE_NAME, ACCESS_COOKIE_AGE_SECONDS, accessToken);
        response.setHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        response.setHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
    }

    private ResponseCookie makeCookie(
            final String cookieName, final int cookieAge, final String token) {
        return ResponseCookie.from(cookieName, token)
                .maxAge(cookieAge)
                .secure(false)
                .httpOnly(true)
                .build();
    }
}
