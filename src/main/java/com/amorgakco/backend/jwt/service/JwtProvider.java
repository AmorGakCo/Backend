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
    public JwtProvider(JwtProperties props) {
        this.secretKey = Keys.hmacShaKeyFor(props.secretKey().getBytes(StandardCharsets.UTF_8));
    }

    private final SecretKey secretKey;

    public String create(String subject, Long duration) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + duration);
        return Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    public void checkAccessToken(String accessToken) {
        try {
            parse(accessToken);
        } catch (ExpiredJwtException e) {
            throw new AccessTokenExpiredException();
        } catch (JwtException e) {
            throw new InvalidJwtException();
        }
    }

    private Jws<Claims> parse(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
    }

    public void checkRefreshToken(String refreshToken) {
        try {
            parse(refreshToken);
        } catch (ExpiredJwtException e) {
            throw new RefreshTokenExpiredException();
        } catch (JwtException e) {
            throw new InvalidJwtException();
        }
    }

    public String getSubject(String token) {
        return parse(token).getPayload().getSubject();
    }
}
