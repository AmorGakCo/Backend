package com.amorgakco.backend.jwt.service;

import com.amorgakco.backend.global.exception.ResourceNotFoundException;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtExtractor {
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final int TOKEN_PREFIX_LENGTH = TOKEN_PREFIX.length();

    public String extractAccessToken(final String accessTokenWithBearer) {
        if (StringUtils.hasText(accessTokenWithBearer)
                && accessTokenWithBearer.startsWith(TOKEN_PREFIX)) {
            return accessTokenWithBearer.substring(TOKEN_PREFIX_LENGTH);
        }
        throw ResourceNotFoundException.accessTokenNotFound();
    }
}
