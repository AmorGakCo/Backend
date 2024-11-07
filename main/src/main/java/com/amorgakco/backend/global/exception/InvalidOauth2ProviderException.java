package com.amorgakco.backend.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class InvalidOauth2ProviderException extends IllegalArgumentException {

    private final ErrorCode errorCode;

    public static InvalidOauth2ProviderException invalidOauth2Provider() {
        return new InvalidOauth2ProviderException(ErrorCode.ACCESS_TOKEN_EXPIRED);
    }
}
