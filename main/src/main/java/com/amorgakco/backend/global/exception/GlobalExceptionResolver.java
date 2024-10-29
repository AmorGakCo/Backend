package com.amorgakco.backend.global.exception;

import com.amorgakco.backend.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse checkParameter(final MethodArgumentNotValidException e) {
        setExceptionLog(e.getMessage());
        FieldError fieldError = e.getBindingResult().getFieldError();
        if (Objects.isNull(fieldError)) {
            return new ErrorResponse("잘못된 입력입니다.");
        }
        return new ErrorResponse(fieldError.getDefaultMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse accessDenied(final AccessDeniedException e) {
        setExceptionLog(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(GroupAuthorityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidGroupAuthority(final GroupAuthorityException e) {
        setCustomExceptionLog(e.getErrorCode());
        return new ErrorResponse(e.getErrorCode());
    }

    private void setCustomExceptionLog(final ErrorCode errorCode) {
        log.error("Error Code : {} , Message : {}", errorCode.getCode(), errorCode.getMessage());
    }

    @ExceptionHandler(RetryFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse retryFailed(final RetryFailedException e) {
        setCustomExceptionLog(e.getErrorCode());
        return new ErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse resourceNotFound(final ResourceNotFoundException e) {
        setCustomExceptionLog(e.getErrorCode());
        return new ErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(IllegalTimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidTime(final IllegalTimeException e) {
        setCustomExceptionLog(e.getErrorCode());
        return new ErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse jwtAuthentication(final JwtAuthenticationException e) {
        setCustomExceptionLog(e.getErrorCode());
        return new ErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(ParticipantException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse participantException(final ParticipantException e) {
        setCustomExceptionLog(e.getErrorCode());
        return new ErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(MemberTemperatureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidMemberTemperature(final MemberTemperatureException e) {
        setCustomExceptionLog(e.getErrorCode());
        return new ErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(InvalidOauth2ProviderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidOauth2Provider(final InvalidOauth2ProviderException e) {
        setCustomExceptionLog(e.getErrorCode());
        return new ErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(LocationVerificationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidLocationVerification(final LocationVerificationException e) {
        setCustomExceptionLog(e.getErrorCode());
        return new ErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(GroupSearchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidGroupSearch(final GroupSearchException e) {
        setCustomExceptionLog(e.getErrorCode());
        return new ErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(GroupCapacityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidGroupCapacity(final GroupCapacityException e) {
        setCustomExceptionLog(e.getErrorCode());
        return new ErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(DuplicatedRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse duplicatedRequest(final DuplicatedRequestException e) {
        setCustomExceptionLog(e.getErrorCode());
        return new ErrorResponse(e.getErrorCode());
    }
}
