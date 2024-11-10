package com.amorgakco.backend.global.exception;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ParticipantException extends IllegalStateException {

    private final ErrorCode errorCode;

    public static ParticipantException exceedParticipationLimit() {
        return new ParticipantException(ErrorCode.EXCEED_PARTICIPATION_LIMIT);
    }

    public static ParticipantException notSameGroupParticipant() {
        return new ParticipantException(ErrorCode.NOT_SAME_GROUP_PARTICIPANT);
    }

    public static ParticipantException notParticipatedInGroup() {
        return new ParticipantException(ErrorCode.NOT_PARTICIPATED_IN_GROUP);
    }
}
