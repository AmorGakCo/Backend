package com.amorgakco.backend.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class IllegalTimeException extends IllegalStateException {

    private final ErrorCode errorCode;

    public static IllegalTimeException startTimeAfterEndTime() {
        return new IllegalTimeException(ErrorCode.START_TIME_AFTER_ENT_TIME);
    }

    public static IllegalTimeException maxDuration() {
        return new IllegalTimeException(ErrorCode.MAX_DURATION);
    }

    public static IllegalTimeException minDuration() {
        return new IllegalTimeException(ErrorCode.MIN_DURATION);
    }
}
