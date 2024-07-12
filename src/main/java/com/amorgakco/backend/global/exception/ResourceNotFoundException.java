package com.amorgakco.backend.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.NoSuchElementException;

@Getter
@RequiredArgsConstructor
public class ResourceNotFoundException extends NoSuchElementException {
    private final ErrorCode errorcode;
}
