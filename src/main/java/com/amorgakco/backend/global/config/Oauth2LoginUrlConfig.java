package com.amorgakco.backend.global.config;

import com.amorgakco.backend.oauth2.provider.kakao.KakaoOauth2Properties;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@RequiredArgsConstructor
public class Oauth2LoginUrlConfig {
    private final KakaoOauth2Properties kakaoOauth2Properties;

    @Bean
    public KakaoRedirectionLoginUrl redirectionLoginUrl() {
        return new KakaoRedirectionLoginUrl(
                UriComponentsBuilder.fromUriString(kakaoOauth2Properties.loginUri())
                        .queryParam("response_type", "code")
                        .queryParam("client_id", kakaoOauth2Properties.clientId())
                        .queryParam("redirect_uri", kakaoOauth2Properties.redirectUri())
                        .queryParam("scope", String.join(",", kakaoOauth2Properties.scope()))
                        .toUriString());
    }
}
