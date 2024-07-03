package com.amorgakco.backend.oauth.jwt.filter;

import com.amorgakco.backend.oauth.jwt.exception.InvalidHeaderException;
import com.amorgakco.backend.oauth.jwt.service.JwtValidator;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final int TOKEN_PREFIX_LENGTH = 7;
    private final JwtValidator jwtValidator;

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain)
            throws ServletException, IOException {
        final String accessToken = extractAccessToken(request);
        final Authentication authentication = jwtValidator.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private String extractAccessToken(final HttpServletRequest request) {
        final String token = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX_LENGTH);
        } else {
            throw new InvalidHeaderException();
        }
    }
}
