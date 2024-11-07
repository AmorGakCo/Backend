package com.amorgakco.backend.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupAuthorityException extends IllegalStateException {

    private final ErrorCode errorCode;

    public static GroupAuthorityException noAuthorityForGroup() {
        return new GroupAuthorityException(ErrorCode.NO_AUTHORITY_FOR_GROUP);
    }
}
