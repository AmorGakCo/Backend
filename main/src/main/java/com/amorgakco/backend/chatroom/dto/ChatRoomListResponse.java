package com.amorgakco.backend.chatroom.dto;

import com.amorgakco.backend.chatroomparticipant.dto.ChatRoomParticipantResponse;
import java.util.List;
import lombok.Builder;

@Builder
public record ChatRoomListResponse(
    int page,
    int elementSize,
    boolean hasNext,
    List<ChatRoomResponse> chatRoomResponses
) {

}
