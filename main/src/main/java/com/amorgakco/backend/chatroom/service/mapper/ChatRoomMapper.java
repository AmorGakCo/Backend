package com.amorgakco.backend.chatroom.service.mapper;

import com.amorgakco.backend.chatroom.domain.ChatRoom;
import com.amorgakco.backend.chatroom.dto.ChatRoomPageResponse;
import com.amorgakco.backend.chatroom.dto.ChatRoomResponse;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatRoomMapper {

    public ChatRoomPageResponse toChatRoomListResponse(final Slice<ChatRoom> chatRoomSlice) {
        return ChatRoomPageResponse.builder()
                .chatRooms(getChatRoomResponses(chatRoomSlice))
                .elementSize(chatRoomSlice.getSize())
                .hasNext(chatRoomSlice.hasNext())
                .page(chatRoomSlice.getPageable().getPageNumber())
                .build();
    }

    private List<ChatRoomResponse> getChatRoomResponses(final Slice<ChatRoom> chatRoomSlice) {
        return chatRoomSlice.getContent()
                .stream()
                .map(this::toChatRoomResponse)
                .collect(Collectors.toList());
    }

    private ChatRoomResponse toChatRoomResponse(final ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getId())
                .groupName(chatRoom.getGroup().getName())
                .build();
    }
}
