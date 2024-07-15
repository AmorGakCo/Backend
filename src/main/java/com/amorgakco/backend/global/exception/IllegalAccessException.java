package com.amorgakco.backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class IllegalAccessException extends IllegalStateException {
    private final ErrorCode errorcode;
}
