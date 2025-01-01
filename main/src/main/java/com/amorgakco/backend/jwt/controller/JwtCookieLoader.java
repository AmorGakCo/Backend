package com.amorgakco.backend.jwt.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriBuilder;

@Component
@Slf4j
public class JwtCookieLoader {

    private static final int REFRESH_COOKIE_AGE_SECONDS = 604800;
    private static final String REFRESH_COOKIE_NAME = "refresh-token";

    public void loadCookie(final HttpServletResponse response,
        final String token) {
        final ResponseCookie refreshTokenCookie = makeCookie(token);
        response.setHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    private ResponseCookie makeCookie(final String token) {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, token)
            .maxAge(REFRESH_COOKIE_AGE_SECONDS)
            .secure(true)
            .httpOnly(true)
            .sameSite("none")
            .path("/")
            .build();
    }
}
