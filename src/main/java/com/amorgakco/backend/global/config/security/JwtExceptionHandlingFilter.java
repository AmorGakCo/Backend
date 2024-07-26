package com.amorgakco.backend.global.config.security;

import com.amorgakco.backend.global.exception.JwtAuthenticationException;
import com.amorgakco.backend.global.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtExceptionHandlingFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (final JwtAuthenticationException e) {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            final String errorResponse =
                    objectMapper.writeValueAsString(new ErrorResponse(e.getErrorCode()));
            response.setStatus(401);
            response.getWriter().write(errorResponse);
        }
    }
}
