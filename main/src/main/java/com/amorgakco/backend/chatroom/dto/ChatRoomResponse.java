package com.amorgakco.backend.chatroom.dto;

import com.amorgakco.backend.chatroomparticipant.dto.ChatRoomParticipantResponse;
import lombok.Builder;

import java.util.List;
import java.util.Set;

@Builder
public record ChatRoomResponse(
        String groupName,
        Long chatRoomId,
        Set<ChatRoomParticipantResponse> chatRoomParticipants) {
}
