package com.amorgakco.backend.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DuplicatedRequestException extends IllegalStateException{

    private final ErrorCode errorCode;

    public static DuplicatedRequestException duplicatedParticipant() {
        return new DuplicatedRequestException(ErrorCode.PARTICIPANT_DUPLICATED);
    }

    public static DuplicatedRequestException duplicatedGroupApplication() {
        return new DuplicatedRequestException(ErrorCode.GROUP_APPLICATION_DUPLICATED);
    }
}
