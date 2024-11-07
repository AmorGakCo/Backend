package com.amorgakco.backend.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class IllegalFormatException extends IllegalArgumentException {

    private final ErrorCode errorCode;

    public static IllegalFormatException dashNotAllowed() {
        return new IllegalFormatException(ErrorCode.DASH_NOT_ALLOWED);
    }

    public static IllegalFormatException invalidGithubUrl() {
        return new IllegalFormatException(ErrorCode.INVALID_GITHUB_URL);
    }
}
