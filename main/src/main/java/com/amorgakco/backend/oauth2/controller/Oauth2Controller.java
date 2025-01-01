package com.amorgakco.backend.oauth2.controller;

import com.amorgakco.backend.global.config.KakaoRedirectionLoginUrl;
import com.amorgakco.backend.global.config.LocalKakaoRedirectionLoginUrl;
import com.amorgakco.backend.jwt.controller.JwtCookieLoader;
import com.amorgakco.backend.jwt.dto.MemberTokens;
import com.amorgakco.backend.jwt.service.JwtService;
import com.amorgakco.backend.member.domain.Oauth2ProviderType;
import com.amorgakco.backend.oauth2.dto.Oauth2LoginResponse;
import com.amorgakco.backend.oauth2.dto.Oauth2MemberResponse;
import com.amorgakco.backend.oauth2.service.Oauth2Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/oauth2")
@Slf4j
public class Oauth2Controller {

    private final Oauth2Service oauth2Service;
    private final JwtService jwtService;
    private final JwtCookieLoader jwtCookieLoader;

    private final LocalKakaoRedirectionLoginUrl localKakaoRedirectionLoginUrl;
    private final KakaoRedirectionLoginUrl kakaoRedirectionLoginUrl;

    @GetMapping("/{oauth2ProviderType}")
    public void redirectOauth2LoginUrl(
        @PathVariable final Oauth2ProviderType oauth2ProviderType,
        final HttpServletRequest request,
        final HttpServletResponse response)
        throws IOException, URISyntaxException {
        String redirectionUrl = getLoginUrl(request,
            oauth2ProviderType);
        response.sendRedirect(redirectionUrl);
    }

    @PostMapping("/{oauth2ProviderType}")
    @ResponseStatus(HttpStatus.CREATED)
    public Oauth2LoginResponse login(
        @PathVariable final Oauth2ProviderType oauth2ProviderType,
        @RequestParam final String authCode,
        final HttpServletRequest request,
        final HttpServletResponse response) throws URISyntaxException {
        final String redirectionUrl = getRedirectUrl(request);
        final Oauth2MemberResponse oauth2MemberResponse =
            oauth2Service.login(oauth2ProviderType, authCode, redirectionUrl);
        final MemberTokens tokens =
            jwtService.createAndSaveMemberTokens(oauth2MemberResponse.memberId());
        jwtCookieLoader.loadCookie(response, tokens.refreshToken());
        return new Oauth2LoginResponse(oauth2MemberResponse, tokens.accessToken());
    }

    @GetMapping("/test")



    private String getLoginUrl(final HttpServletRequest request,
        final Oauth2ProviderType oauth2ProviderType) throws URISyntaxException {
        String referer = request.getHeader(HttpHeaders.REFERER);
        if (new URI(referer).getHost().equals("localhost")) {
            return localKakaoRedirectionLoginUrl.redirectionUrl();
        } else {
            return oauth2Service.getRedirectionLoginUrl(oauth2ProviderType);
        }
    }

    private String getRedirectUrl(final HttpServletRequest request) throws URISyntaxException {
        String referer = request.getHeader(HttpHeaders.REFERER);
        if (new URI(referer).getHost().equals("localhost")) {
            return "http://localhost:3000/redirected/kakao";
        } else {
            return "https://amorgakco.store/redirected/kakao";
        }
    }
}
