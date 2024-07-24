package com.amorgakco.backend.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceNotFoundException extends NoSuchElementException {
    private final ErrorCode errorCode;

    public static ResourceNotFoundException memberNotFound() {
        return new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
    }

    public static ResourceNotFoundException groupNotFound() {
        return new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
    }

    public static ResourceNotFoundException refreshTokenNotFound() {
        return new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
    }

    public static ResourceNotFoundException accessTokenNotFound() {
        return new ResourceNotFoundException(ErrorCode.ACCESS_TOKEN_NOT_FOUND);
    }

    public static ResourceNotFoundException participantsNotFound() {
        return new ResourceNotFoundException(ErrorCode.PARTICIPANTS_NOT_FOUND);
    }
}
