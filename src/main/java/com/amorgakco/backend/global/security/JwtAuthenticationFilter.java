package com.amorgakco.backend.global.security;

import com.amorgakco.backend.jwt.service.JwtExtractor;
import com.amorgakco.backend.jwt.service.JwtValidator;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtValidator jwtValidator;
    private final JwtExtractor jwtExtractor;

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain)
            throws ServletException, IOException {
        final String accessTokenWithBearer = request.getHeader(AUTHORIZATION_HEADER);
        final String token = jwtExtractor.extractAccessToken(accessTokenWithBearer);
        final Authentication authentication = jwtValidator.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
