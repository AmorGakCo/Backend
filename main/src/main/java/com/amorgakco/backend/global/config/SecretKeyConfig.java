package com.amorgakco.backend.global.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Configuration
public class SecretKeyConfig {
    @Bean
    public SecretKey secretKey(final @Value("${jwt.secret-key}") String jwtSign) {
        return Keys.hmacShaKeyFor(jwtSign.getBytes(StandardCharsets.UTF_8));
    }
}
