package com.amorgakco.backend.global.oauth.handler;

import com.amorgakco.backend.jwt.controller.JwtCookieLoader;
import com.amorgakco.backend.jwt.dto.AccessTokenResponse;
import com.amorgakco.backend.jwt.dto.MemberJwt;
import com.amorgakco.backend.jwt.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
@Component
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final JwtCookieLoader jwtCookieLoader;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication)
            throws IOException {
        final MemberJwt memberJwt = jwtService.createAndSaveMemberToken(authentication.getName());
        jwtCookieLoader.loadCookie(response, memberJwt.refreshToken());
        final String accessToken =
                objectMapper.writeValueAsString(new AccessTokenResponse(memberJwt.accessToken()));
        final PrintWriter writer = response.getWriter();
        writer.write(accessToken);
        //        프론트랑 붙일 때 필요함
        //        response.sendRedirect(jwtProperties.redirectUri());
        writer.flush();
    }
}
