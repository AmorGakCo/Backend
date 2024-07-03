package com.amorgakco.backend.oauth.jwt.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtCreator {

    public JwtCreator(final JwtProperties props) {
        this.secretKey = Keys.hmacShaKeyFor(props.secretKey().getBytes(StandardCharsets.UTF_8));
    }

    private SecretKey secretKey;

    public String create(final String memberId, final Long duration) {
        final Date now = new Date();
        final Date expirationDate = new Date(now.getTime() + duration);
        return Jwts.builder()
                .subject(memberId)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }
}
