package com.amorgakco.backend.chatroom.dto;

import lombok.Builder;

@Builder
public record ChatRoomResponse(
        Long chatRoomId,
        String groupName) {
}
