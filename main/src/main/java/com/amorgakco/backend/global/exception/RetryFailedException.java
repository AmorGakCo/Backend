package com.amorgakco.backend.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RetryFailedException extends IllegalStateException {

    private final ErrorCode errorCode;

    public static RetryFailedException retryFailed() {
        return new RetryFailedException(ErrorCode.RETRY_FAILED);
    }
}
