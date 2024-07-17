package com.amorgakco.backend.global.exception;

import lombok.Getter;

@Getter
public class JwtAuthenticationException extends IllegalStateException {
    private final ErrorCode errorcode;

    private JwtAuthenticationException(final ErrorCode errorcode) {
        this.errorcode = errorcode;
    }

    public static JwtAuthenticationException accessTokenExpired() {
        return new JwtAuthenticationException(ErrorCode.ACCESS_TOKEN_EXPIRED);
    }

    public static JwtAuthenticationException canNotParseToken() {
        return new JwtAuthenticationException(ErrorCode.CANNOT_PARSE_TOKEN);
    }

    public static JwtAuthenticationException memberNotFound() {
        throw new JwtAuthenticationException(ErrorCode.MEMBER_NOT_FOUND);
    }
}
