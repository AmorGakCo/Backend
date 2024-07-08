package com.amorgakco.backend.jwt.controller;

import com.amorgakco.backend.global.oauth.handler.JwtCookieLoader;
import com.amorgakco.backend.jwt.dto.AccessTokenResponse;
import com.amorgakco.backend.jwt.dto.MemberJwt;
import com.amorgakco.backend.jwt.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class JwtController {

    private final JwtService jwtService;
    private final JwtCookieLoader jwtCookieLoader;

    @PostMapping("/token/reissue")
    public AccessTokenResponse reissue(
            @CookieValue(value = "refresh-token") final Cookie cookie,
            @RequestHeader(value = "Authorization") final String accessTokenHeader,
            final HttpServletResponse response) {
        final MemberJwt memberJwt =
                jwtService.reissue(Optional.ofNullable(cookie), accessTokenHeader);
        jwtCookieLoader.loadCookie(response, memberJwt.refreshToken());
        return new AccessTokenResponse(memberJwt.accessToken());
    }

    @DeleteMapping("/logout")
    public void logout(@CookieValue(value = "refresh-token") final Cookie cookie) {
        jwtService.logout(Optional.ofNullable(cookie));
    }
}
