package com.amorgakco.backend.global.oauth.handler;

import com.amorgakco.backend.jwt.controller.JwtCookieLoader;
import com.amorgakco.backend.jwt.dto.MemberJwt;
import com.amorgakco.backend.jwt.service.JwtProperties;
import com.amorgakco.backend.jwt.service.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final JwtCookieLoader jwtCookieLoader;
    private final JwtProperties jwtProperties;

    @Override
    public void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication)
            throws IOException {
        final MemberJwt memberJwt = jwtService.createAndSaveMemberToken(authentication.getName());
        jwtCookieLoader.loadCookies(response, memberJwt.refreshToken(), memberJwt.accessToken());
        response.sendRedirect(jwtProperties.frontUrl());
    }
}
