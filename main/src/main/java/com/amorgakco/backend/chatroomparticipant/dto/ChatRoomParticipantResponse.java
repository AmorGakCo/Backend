package com.amorgakco.backend.chatroomparticipant.dto;

import lombok.Builder;

@Builder
public record ChatRoomParticipantResponse(
        Long memberId,
        String nickname,
        String imgUrl
) {
}
