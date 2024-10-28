package com.amorgakco.backend.global.exception;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupSearchException extends IllegalArgumentException {

    private final ErrorCode errorCode;

    public static GroupSearchException invalidDiagonalDistance() {
        return new GroupSearchException(ErrorCode.INVALID_DIAGONAL_DISTANCE);
    }
}
