package com.amorgakco.backend.chatroom.service.mapper;

import com.amorgakco.backend.chatroom.domain.ChatRoom;
import com.amorgakco.backend.chatroom.dto.ChatRoomResponse;
import com.amorgakco.backend.chatroom.dto.ChatRoomListResponse;
import com.amorgakco.backend.chatroom.dto.ChatRoomSubjectResponse;
import com.amorgakco.backend.chatroomparticipant.domain.ChatRoomParticipant;
import com.amorgakco.backend.chatroomparticipant.dto.ChatRoomParticipantResponse;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ChatRoomMapper {

    public ChatRoomListResponse toChatRoomSliceResponse(final Slice<ChatRoom> chatRoomSlice) {
        return ChatRoomListResponse.builder()
                .chatRoomSubjects(getSubjects(chatRoomSlice))
                .elementSize(chatRoomSlice.getSize())
                .hasNext(chatRoomSlice.hasNext())
                .page(chatRoomSlice.getPageable().getPageNumber())
                .build();
    }

    private List<ChatRoomSubjectResponse> getSubjects(final Slice<ChatRoom> chatRoomSlice) {
        return chatRoomSlice.getContent()
                .stream()
                .map(this::toChatRoomSubjectResponse)
                .collect(Collectors.toList());
    }

    private ChatRoomSubjectResponse toChatRoomSubjectResponse(final ChatRoom chatRoom) {
        return ChatRoomSubjectResponse.builder()
                .chatRoomId(chatRoom.getId())
                .groupName(chatRoom.getGroup().getName())
                .build();
    }

    public ChatRoomResponse toChatRoomResponse(final ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .groupName(chatRoom.getGroup().getName())
                .chatRoomParticipants(getParticipants(chatRoom))
                .build();
    }

    private Set<ChatRoomParticipantResponse> getParticipants(final ChatRoom chatRoom) {
        return chatRoom.getChatRoomParticipants().stream().map(this::toChatRoomParticipantResponse).collect(Collectors.toSet());
    }

    private ChatRoomParticipantResponse toChatRoomParticipantResponse(final ChatRoomParticipant participant) {
        return ChatRoomParticipantResponse.builder()
                .participantId(participant.getId())
                .imgUrl(participant.getNickname())
                .imgUrl(participant.getImgUrl())
                .build();
    }
}
