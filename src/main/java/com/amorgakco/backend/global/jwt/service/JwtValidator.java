package com.amorgakco.backend.global.jwt.service;

import com.amorgakco.backend.global.jwt.exception.InvalidJwtException;
import com.amorgakco.backend.global.jwt.exception.TokenExpiredException;
import com.amorgakco.backend.member.exception.MemberNotFoundException;
import com.amorgakco.backend.member.repository.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtValidator {
    private static final String EMPTY_CREDENTIAL = "";
    private final MemberRepository memberRepository;
    private final SecretKey secretKey;

    @Autowired
    public JwtValidator(final JwtProperties jwtProperties, final MemberRepository memberRepository) {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes(StandardCharsets.UTF_8));
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
