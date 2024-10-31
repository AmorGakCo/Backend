package com.amorgakco.backend.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberTemperatureException extends IllegalStateException {

    private final ErrorCode errorCode;

    public static MemberTemperatureException exceedMaxTemperature() {
        return new MemberTemperatureException(ErrorCode.EXCEED_MAX_TEMPERATURE);
    }

    public static MemberTemperatureException underMinTemperature() {
        return new MemberTemperatureException(ErrorCode.UNDER_MIN_TEMPERATURE);
    }
}
