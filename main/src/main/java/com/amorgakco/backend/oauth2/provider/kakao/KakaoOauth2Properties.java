package com.amorgakco.backend.oauth2.provider.kakao;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth2.kakao")
public record KakaoOauth2Properties(
    String loginUri,
    String redirectUri,
    String clientId,
    String clientSecret,
    String[] scope) {

}
