package com.amorgakco.backend.oauth.jwt.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secretKey, Long accessExpiration, Long refreshExpiration) {}
