package com.amorgakco.backend.global.exception;

import lombok.Getter;

import org.springframework.security.core.AuthenticationException;

@Getter
public class JwtAuthenticationException extends AuthenticationException {
    private final ErrorCode errorcode;

    public JwtAuthenticationException(final ErrorCode errorcode) {
        super(errorcode.getMessage());
        this.errorcode = errorcode;
    }
}
