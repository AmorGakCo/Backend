package com.amorgakco.backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class InvalidTokenException extends IllegalArgumentException {
    private final ErrorCode errorcode;
}
