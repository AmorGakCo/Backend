package com.amorgakco.backend.chatroom.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ChatRoomListResponse(
        int page,
        int elementSize,
        boolean hasNext,
        List<ChatRoomSubjectResponse> chatRoomSubjects
) {
}
