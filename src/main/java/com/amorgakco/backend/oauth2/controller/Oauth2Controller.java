package com.amorgakco.backend.oauth2.controller;

import com.amorgakco.backend.jwt.controller.JwtCookieLoader;
import com.amorgakco.backend.jwt.dto.MemberTokens;
import com.amorgakco.backend.jwt.service.JwtService;
import com.amorgakco.backend.member.domain.Oauth2ProviderType;
import com.amorgakco.backend.oauth2.dto.Oauth2LoginResponse;
import com.amorgakco.backend.oauth2.dto.Oauth2MemberResponse;
import com.amorgakco.backend.oauth2.service.Oauth2Service;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2")
public class Oauth2Controller {

    private final Oauth2Service oauth2Service;
    private final JwtService jwtService;
    private final JwtCookieLoader jwtCookieLoader;

    @GetMapping("/{oauth2ProviderType}")
    public void redirectOauth2LoginUrl(
            @PathVariable final Oauth2ProviderType oauth2ProviderType,
            final HttpServletResponse response)
            throws IOException {
        final String loginUrl = oauth2Service.getRedirectionLoginUrl(oauth2ProviderType);
        response.sendRedirect(loginUrl);
    }

    @PostMapping("/{oauth2ProviderType}")
    @ResponseStatus(HttpStatus.CREATED)
    public Oauth2LoginResponse login(
            @PathVariable final Oauth2ProviderType oauth2ProviderType,
            @RequestParam final String authCode,
            final HttpServletResponse response) {
        final Oauth2MemberResponse oauth2MemberResponse =
                oauth2Service.login(oauth2ProviderType, authCode);
        final MemberTokens tokens =
                jwtService.createAndSaveMemberTokens(oauth2MemberResponse.memberId());
        jwtCookieLoader.loadCookie(response, tokens.refreshToken());
        return new Oauth2LoginResponse(oauth2MemberResponse, tokens.accessToken());
    }
}
