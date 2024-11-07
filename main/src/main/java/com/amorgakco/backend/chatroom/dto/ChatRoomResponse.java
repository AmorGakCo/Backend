package com.amorgakco.backend.chatroom.dto;

import com.amorgakco.backend.chatroomparticipant.dto.ChatRoomParticipantResponse;
import java.util.Set;
import lombok.Builder;

@Builder
public record ChatRoomResponse(
    String groupName,
    Long chatRoomId,
    Set<ChatRoomParticipantResponse> chatRoomParticipants) {

}
