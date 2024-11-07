package com.amorgakco.backend.fixture.chatroom;

import com.amorgakco.backend.chatroom.domain.ChatRoom;
import com.amorgakco.backend.chatroom.dto.ChatRoomListResponse;
import com.amorgakco.backend.chatroom.dto.ChatRoomResponse;
import com.amorgakco.backend.chatroom.dto.ChatRoomSubjectResponse;
import com.amorgakco.backend.chatroomparticipant.dto.ChatRoomParticipantResponse;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.member.domain.Member;
import java.util.List;
import java.util.Set;

public class TestChatRoomFactory {

    private static final int PAGE = 0;
    private static final Long CHAT_ROOM_ID = 1L;
    private static final String GROUP_NAME = "Amorgakco";
    private static final String IMG_URL = "fakeimg.jpg";
    private static final String NICKNAME = "아모르겠고";


    public static ChatRoom create(final Group group, final Member member) {
        return new ChatRoom(member, group);
    }

    public static ChatRoomResponse getChatRoomResponse(final Long chatRoomId) {
        return ChatRoomResponse.builder()
            .chatRoomId(CHAT_ROOM_ID)
            .groupName(GROUP_NAME)
            .chatRoomParticipants(Set.of(
                getChatRoomParticipantResponse(1L),
                getChatRoomParticipantResponse(2L),
                getChatRoomParticipantResponse(3L)
            ))
            .build();
    }

    public static ChatRoomParticipantResponse getChatRoomParticipantResponse(final Long memberId) {
        return ChatRoomParticipantResponse
            .builder()
            .memberId(memberId)
            .imgUrl(IMG_URL)
            .nickname(NICKNAME)
            .build();
    }

    public static ChatRoomListResponse getChatRoomListResponse() {
        List<ChatRoomSubjectResponse> chatRoomSubjectResponses =
            List.of(getChatRoomSubjectResponse(1L),
                getChatRoomSubjectResponse(2L),
                getChatRoomSubjectResponse(3L));
        return ChatRoomListResponse.builder()
            .page(PAGE)
            .chatRoomSubjects(chatRoomSubjectResponses)
            .elementSize(chatRoomSubjectResponses.size())
            .hasNext(false)
            .build();
    }

    private static ChatRoomSubjectResponse getChatRoomSubjectResponse(final Long chatRoomId) {
        return ChatRoomSubjectResponse.builder()
            .chatRoomId(chatRoomId)
            .groupName(GROUP_NAME)
            .build();
    }
}
