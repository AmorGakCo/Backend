package com.amorgakco.backend.global.response;

import com.amorgakco.backend.global.exception.ErrorCode;

import lombok.Builder;

@Builder
public record ErrorResponse(String status, String code, String message) {
    private static final String FAILURE = "failure";

    public ErrorResponse(final ErrorCode code) {
        this(FAILURE, code.getCode(), code.getMessage());
    }

    public ErrorResponse(final String message) {
        this(FAILURE, null, message);
    }
}
