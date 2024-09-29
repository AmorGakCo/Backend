package com.amorgakco.backend.jwt.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(Long accessExpiration, Long refreshExpiration, String frontUrl) {
}
