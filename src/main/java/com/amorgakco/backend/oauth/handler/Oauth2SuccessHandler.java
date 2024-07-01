package com.amorgakco.backend.oauth.handler;

import com.amorgakco.backend.jwt.dto.MemberJwt;
import com.amorgakco.backend.jwt.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${token-redirect-uri}")
    private String tokenRedirectUri;

    private static final String ACCESS_TOKEN = "access-token";
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication)
            throws IOException {
        final MemberJwt memberJwt =
                jwtService.createMemberToken(Long.parseLong(authentication.getName()));
        JwtCookieLoader.loadCookie(response, memberJwt.refreshToken());
        response.sendRedirect(createUriWithAccessToken(memberJwt.accessToken()));
    }

    private String createUriWithAccessToken(final String accessToken) {
        return UriComponentsBuilder.fromUriString(tokenRedirectUri)
                .queryParam(ACCESS_TOKEN, accessToken)
                .build()
                .toUriString();
    }
}
