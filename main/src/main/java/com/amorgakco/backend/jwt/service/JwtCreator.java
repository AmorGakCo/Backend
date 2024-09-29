package com.amorgakco.backend.jwt.service;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtCreator {
    private final SecretKey secretKey;

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
