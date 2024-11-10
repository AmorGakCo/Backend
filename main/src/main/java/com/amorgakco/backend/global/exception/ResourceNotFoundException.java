package com.amorgakco.backend.global.exception;

import java.util.NoSuchElementException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceNotFoundException extends NoSuchElementException {

    private final ErrorCode errorCode;

    public static ResourceNotFoundException memberNotFound() {
        return new ResourceNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
    }

    public static ResourceNotFoundException participationNotFound() {
        return new ResourceNotFoundException(ErrorCode.PARTICIPATION_NOT_FOUND);
    }

    public static ResourceNotFoundException groupNotFound() {
        return new ResourceNotFoundException(ErrorCode.GROUP_NOT_FOUND);
    }

    public static ResourceNotFoundException refreshTokenNotFound() {
        return new ResourceNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }

    public static ResourceNotFoundException groupParticipantsNotFound() {
        return new ResourceNotFoundException(ErrorCode.GROUP_PARTICIPANT_NOT_FOUND);
    }

    public static ResourceNotFoundException chatRoomNotFound() {
        return new ResourceNotFoundException(ErrorCode.CHAT_ROOM_NOT_FOUND);
    }

    public static ResourceNotFoundException chatRoomParticipantsNotFound() {
        return new ResourceNotFoundException(ErrorCode.CHAT_ROOM_PARTICIPANT_NOT_FOUND);
    }
}
