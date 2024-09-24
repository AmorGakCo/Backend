package com.amorgakco.backend.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class IllegalAccessException extends IllegalStateException {
    private final ErrorCode errorCode;

    public static IllegalAccessException refreshTokenRequired() {
        return new IllegalAccessException(ErrorCode.REFRESH_TOKEN_REQUIRED);
    }

    public static IllegalAccessException noAuthorityForGroup() {
        return new IllegalAccessException(ErrorCode.NO_AUTHORITY_FOR_GROUP);
    }

    public static IllegalAccessException verificationFailed() {
        return new IllegalAccessException(ErrorCode.VERIFICATION_FAILED);
    }

    public static IllegalAccessException verificationDuplicated() {
        return new IllegalAccessException(ErrorCode.VERIFICATION_DUPLICATED);
    }

    public static IllegalAccessException duplicatedParticipant() {
        return new IllegalAccessException(ErrorCode.PARTICIPANT_DUPLICATED);
    }

    public static IllegalAccessException exceedGroupCapacity() {
        return new IllegalAccessException(ErrorCode.PARTICIPANT_DUPLICATED);
    }

    public static IllegalAccessException invalidDiagonalDistance() {
        return new IllegalAccessException(ErrorCode.INVALID_DIAGONAL_DISTANCE);
    }

    public static IllegalAccessException notSameGroupParticipant() {
        return new IllegalAccessException(ErrorCode.NOT_SAME_GROUP_PARTICIPANT);
    }

    public static IllegalAccessException canNotExceedPositive100() {
        return new IllegalAccessException(ErrorCode.CAN_NOT_EXCEED_POSITIVE_100);
    }

    public static IllegalAccessException canNotUnderNegative100() {
        return new IllegalAccessException(ErrorCode.CAN_NOT_UNDER_NEGATIVE_100);
    }
}
