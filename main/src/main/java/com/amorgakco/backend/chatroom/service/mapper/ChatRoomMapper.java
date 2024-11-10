package com.amorgakco.backend.chatroom.service.mapper;

import com.amorgakco.backend.chatroom.domain.ChatRoom;
import com.amorgakco.backend.chatroom.dto.ChatRoomListResponse;
import com.amorgakco.backend.chatroom.dto.ChatRoomResponse;
import com.amorgakco.backend.chatroomparticipant.domain.ChatRoomParticipant;
import com.amorgakco.backend.chatroomparticipant.dto.ChatRoomParticipantResponse;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomMapper {

    public ChatRoomListResponse toChatRoomListResponse(final Slice<ChatRoom> chatRoomSlice) {
        return ChatRoomListResponse.builder()
            .chatRoomResponses(
                chatRoomSlice.getContent().stream().map(this::toChatRoomResponse).toList())
            .elementSize(chatRoomSlice.getSize())
            .hasNext(chatRoomSlice.hasNext())
            .page(chatRoomSlice.getPageable().getPageNumber())
            .build();
    }

    public ChatRoomResponse toChatRoomResponse(final ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
            .chatRoomId(chatRoom.getId())
            .groupName(chatRoom.getGroup().getName())
            .chatRoomParticipants(getParticipants(chatRoom))
            .build();
    }

    private Set<ChatRoomParticipantResponse> getParticipants(final ChatRoom chatRoom) {
        return chatRoom.getChatRoomParticipants().stream().map(this::toChatRoomParticipantResponse)
            .collect(Collectors.toSet());
    }

    private ChatRoomParticipantResponse toChatRoomParticipantResponse(
        final ChatRoomParticipant participant) {
        return ChatRoomParticipantResponse.builder()
            .memberId(participant.getId())
            .imgUrl(participant.getNickname())
            .imgUrl(participant.getImgUrl())
            .build();
    }
}
