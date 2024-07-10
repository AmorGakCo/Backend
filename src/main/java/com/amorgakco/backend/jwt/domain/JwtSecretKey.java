package com.amorgakco.backend.jwt.domain;

import io.jsonwebtoken.security.Keys;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

@Component
@Getter
public class JwtSecretKey {
    private final SecretKey secretKey;

    @Autowired
    protected JwtSecretKey(final @Value("${jwt.secret-key}") String jwtSign) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSign.getBytes(StandardCharsets.UTF_8));
    }
}
