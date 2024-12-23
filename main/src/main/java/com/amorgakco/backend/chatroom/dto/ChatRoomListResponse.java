package com.amorgakco.backend.chatroom.dto;

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
