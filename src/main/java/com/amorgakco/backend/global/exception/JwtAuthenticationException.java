package com.amorgakco.backend.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtAuthenticationException extends IllegalStateException {
    private final ErrorCode errorcode;

    public static JwtAuthenticationException accessTokenExpired() {
        return new JwtAuthenticationException(ErrorCode.ACCESS_TOKEN_EXPIRED);
    }

    public static JwtAuthenticationException canNotParseToken() {
        return new JwtAuthenticationException(ErrorCode.CANNOT_PARSE_TOKEN);
    }

    public static JwtAuthenticationException memberNotFound() {
        throw new JwtAuthenticationException(ErrorCode.MEMBER_NOT_FOUND);
    }

    public static JwtAuthenticationException checkYourToken() {
        throw new JwtAuthenticationException(ErrorCode.CHECK_YOUR_TOKEN);
    }
}
