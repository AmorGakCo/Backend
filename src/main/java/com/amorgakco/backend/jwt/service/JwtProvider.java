package com.amorgakco.backend.jwt.service;

import com.amorgakco.backend.jwt.exception.AccessTokenExpiredException;
import com.amorgakco.backend.jwt.exception.InvalidJwtException;
import com.amorgakco.backend.jwt.exception.RefreshTokenExpiredException;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtProvider {
    public JwtProvider(final JwtProperties props) {
        this.secretKey = Keys.hmacShaKeyFor(props.secretKey().getBytes(StandardCharsets.UTF_8));
    }

    private final SecretKey secretKey;

    public String create(final String subject, final Long duration) {
        final Date now = new Date();
        final Date expirationDate = new Date(now.getTime() + duration);
        return Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    public void checkAccessToken(final String accessToken) {
        try {
            parse(accessToken);
        } catch (ExpiredJwtException e) {
            throw new AccessTokenExpiredException();
        } catch (JwtException e) {
            throw new InvalidJwtException();
        }
    }

    private Jws<Claims> parse(final String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    }

    public void checkRefreshToken(final String refreshToken) {
        try {
            parse(refreshToken);
        } catch (final ExpiredJwtException e) {
            throw new RefreshTokenExpiredException();
        } catch (final JwtException e) {
            throw new InvalidJwtException();
        }
    }

    public String getSubject(final String token) {
        return parse(token).getPayload().getSubject();
    }
}
