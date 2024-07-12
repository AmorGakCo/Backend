package com.amorgakco.backend.global.response;

import com.amorgakco.backend.global.exception.IllegalAccessException;
import com.amorgakco.backend.global.exception.InvalidTokenException;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.global.exception.TokenExpiredException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionResolver {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse unauthorized(final AuthenticationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse accessDenied(final AccessDeniedException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(IllegalAccessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse illegalAccess(final IllegalAccessException e) {
        return new ErrorResponse(e.getErrorcode());
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidToken(final InvalidTokenException e) {
        return new ErrorResponse(e.getErrorcode());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse resourceNotFound(final ResourceNotFoundException e) {
        return new ErrorResponse(e.getErrorcode());
    }

    @ExceptionHandler(TokenExpiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse tokenExpired(final TokenExpiredException e) {
        return new ErrorResponse(e.getErrorcode());
    }
}
