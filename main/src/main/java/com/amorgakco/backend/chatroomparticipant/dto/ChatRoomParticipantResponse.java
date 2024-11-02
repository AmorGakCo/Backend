package com.amorgakco.backend.chatroomparticipant.dto;

import lombok.Builder;

@Builder
public record ChatRoomParticipantResponse(
        Long participantId,
        String nickname,
        String imgUrl
) {
}
