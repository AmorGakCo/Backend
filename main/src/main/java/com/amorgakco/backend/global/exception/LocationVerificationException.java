package com.amorgakco.backend.global.exception;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationVerificationException extends IllegalStateException{

    private final ErrorCode errorCode;

    public static LocationVerificationException verificationFailed() {
        return new LocationVerificationException(ErrorCode.VERIFICATION_FAILED);
    }

    public static LocationVerificationException verificationDuplicated() {
        return new LocationVerificationException(ErrorCode.VERIFICATION_DUPLICATED);
    }

}
