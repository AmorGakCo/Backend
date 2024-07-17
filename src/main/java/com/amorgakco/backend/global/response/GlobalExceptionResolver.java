package com.amorgakco.backend.global.response;

import com.amorgakco.backend.global.exception.*;
import com.amorgakco.backend.global.exception.IllegalAccessException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionResolver {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse unauthorized(final AuthenticationException e) {
        setExceptionLog(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    private void setExceptionLog(final String message) {
        log.error("Error Message : {}", message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse accessDenied(final AccessDeniedException e) {
        setExceptionLog(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    private void setCustomExceptionLog(final ErrorCode errorCode) {
        log.error("Error Code : {} , Message : {}", errorCode.getCode(), errorCode.getMessage());
    }

    @ExceptionHandler(IllegalAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalAccess(final IllegalAccessException e) {
        setCustomExceptionLog(e.getErrorcode());
        return new ErrorResponse(e.getErrorcode());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidToken(final InvalidTokenException e) {
        setCustomExceptionLog(e.getErrorcode());
        return new ErrorResponse(e.getErrorcode());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse resourceNotFound(final ResourceNotFoundException e) {
        setCustomExceptionLog(e.getErrorcode());
        return new ErrorResponse(e.getErrorcode());
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse tokenExpired(final TokenExpiredException e) {
        setCustomExceptionLog(e.getErrorcode());
        return new ErrorResponse(e.getErrorcode());
    }

    @ExceptionHandler(IllegalTimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse tokenExpired(final IllegalTimeException e) {
        setCustomExceptionLog(e.getErrorCode());
        return new ErrorResponse(e.getErrorCode());
    }
}
