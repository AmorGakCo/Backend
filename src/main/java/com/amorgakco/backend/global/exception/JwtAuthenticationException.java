package com.amorgakco.backend.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtAuthenticationException extends IllegalStateException {
    private final ErrorCode errorCode;

    public static JwtAuthenticationException accessTokenExpired() {
        return new JwtAuthenticationException(ErrorCode.ACCESS_TOKEN_EXPIRED);
    }

    public static JwtAuthenticationException canNotParseToken() {
        return new JwtAuthenticationException(ErrorCode.CANNOT_PARSE_TOKEN);
    }

    public static JwtAuthenticationException memberNotFound() {
        return new JwtAuthenticationException(ErrorCode.MEMBER_NOT_FOUND);
    }

    public static JwtAuthenticationException loginAgain() {
        return new JwtAuthenticationException(ErrorCode.LOGIN_AGAIN);
    }
}
