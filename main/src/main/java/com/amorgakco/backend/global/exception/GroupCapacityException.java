package com.amorgakco.backend.global.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupCapacityException extends IllegalStateException {

    private final ErrorCode errorCode;

    public static GroupCapacityException exceedGroupCapacity() {
        return new GroupCapacityException(ErrorCode.EXCEED_GROUP_CAPACITY);
    }
}
