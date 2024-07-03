package com.amorgakco.backend.oauth.jwt.service;

import com.amorgakco.backend.member.exception.MemberNotFoundException;
import com.amorgakco.backend.member.repository.MemberRepository;
import com.amorgakco.backend.oauth.jwt.exception.InvalidJwtException;
import com.amorgakco.backend.oauth.jwt.exception.TokenExpiredException;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class JwtValidator {
    private static final String EMPTY_CREDENTIAL = "";
    private final MemberRepository memberRepository;
    private final SecretKey secretKey;

    @Autowired
    public JwtValidator(final JwtProperties props, final MemberRepository memberRepository) {
        this.secretKey = Keys.hmacShaKeyFor(props.secretKey().getBytes(StandardCharsets.UTF_8));
        this.memberRepository = memberRepository;
    }

    public Authentication getAuthentication(final String token) {
        checkToken(token);
        return memberRepository
                .findById(Long.parseLong(getClaim(token)))
                .map(
                        m ->
                                new UsernamePasswordAuthenticationToken(
                                        m,
                                        EMPTY_CREDENTIAL,
                                        Collections.singletonList(
                                                new SimpleGrantedAuthority(
                                                        m.getRole().toString()))))
                .orElseThrow(MemberNotFoundException::new);
    }

    private void checkToken(final String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        } catch (final ExpiredJwtException e) {
            throw new TokenExpiredException();
        } catch (final JwtException e) {
            throw new InvalidJwtException();
        }
    }

    private String getClaim(final String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
