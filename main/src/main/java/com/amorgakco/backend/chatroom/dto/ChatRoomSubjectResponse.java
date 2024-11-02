package com.amorgakco.backend.chatroom.dto;

import lombok.Builder;

@Builder
public record ChatRoomSubjectResponse(
        Long chatRoomId,
        String groupName) {
}
