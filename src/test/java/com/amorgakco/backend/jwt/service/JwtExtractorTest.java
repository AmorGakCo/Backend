package com.amorgakco.backend.jwt.service;

import static org.assertj.core.api.Assertions.*;

import com.amorgakco.backend.fixture.security.TestSecretKey;
import com.amorgakco.backend.global.exception.ErrorCode;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtExtractorTest {

    private final JwtExtractor jwtExtractor = new JwtExtractor();
    private final JwtCreator jwtCreator = new JwtCreator(TestSecretKey.create());

    @Test
    @DisplayName("Bearer PREFIX를 포함하지 않으면 예외를 발생시킨다.")
    void bearerNotInclude() {
        // given
        final String token = jwtCreator.create("1", 4000L);
        // when & then
        assertThatThrownBy(() -> jwtExtractor.extractAccessToken(token))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ACCESS_TOKEN_NOT_FOUND);
    }

    @Test
    @DisplayName("비어있는 헤더는 예외를 발생시킨다.")
    void tokenNotInclude() {
        // given
        final String emptyToken = "";
        // when & then
        assertThatThrownBy(() -> jwtExtractor.extractAccessToken(emptyToken))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.ACCESS_TOKEN_NOT_FOUND);
    }
}
