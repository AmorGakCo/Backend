package com.amorgakco.backend.chatroom.controller;

import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentRequest;
import static com.amorgakco.backend.docs.ApiDocsUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.amorgakco.backend.chatroom.dto.ChatRoomListResponse;
import com.amorgakco.backend.chatroom.dto.ChatRoomResponse;
import com.amorgakco.backend.chatroom.service.ChatRoomService;
import com.amorgakco.backend.docs.RestDocsTest;
import com.amorgakco.backend.fixture.chatroom.TestChatRoomFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.security.WithMockMember;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ChatRoomController.class)
@WithMockMember
class ChatRoomControllerTest extends RestDocsTest {

    @MockBean
    ChatRoomService chatRoomService;

    @Test
    @DisplayName("사용자는 채팅방에 참여할 수 있다.")
    void participateChatRoom() throws Exception {
        // given
        final Member member = TestMemberFactory.create(1L);
        final Long chatRoomId = 1L;
        ChatRoomResponse response = TestChatRoomFactory.getChatRoomResponse(chatRoomId);
        given(memberService.getMember(1L)).willReturn(member);
        given(chatRoomService.participateChatRoom(member, 1L)).willReturn(response);
        // when
        final ResultActions actions =
            mockMvc.perform(
                post("/chat-rooms/{chatRoomId}", chatRoomId));
        // then
        actions.andExpect(status().isCreated()).andExpect(jsonPath("$.data.chatRoomId").value("1"));
        // docs
        actions.andDo(print())
            .andDo(document("chat-room-participate", getDocumentRequest(), getDocumentResponse(),
                pathParameters(parameterWithName("chatRoomId").description("채팅방 ID"))));
    }

    @Test
    @DisplayName("사용자는 채팅방을 나갈 수 있다.")
    void exitChatRoom() throws Exception {
        // given
        final Member member = TestMemberFactory.create(1L);
        final Long chatRoomId = 1L;
        given(memberService.getMember(1L)).willReturn(member);
        // when
        final ResultActions actions =
            mockMvc.perform(
                delete("/chat-rooms/{chatRoomId}", chatRoomId));
        // then
        actions.andExpect(status().isOk());
        //docs
        actions.andDo(print())
            .andDo(document("chat-room-exit", getDocumentRequest(), getDocumentResponse(),
                pathParameters(parameterWithName("chatRoomId").description("채팅방 ID"))));
    }

    @Test
    @DisplayName("사용자는 소속된 채팅방 목록을 조회할 수 있다.")
    void getChatRoomList() throws Exception {
        // given
        final Member member = TestMemberFactory.create(1L);
        final Integer page = 0;
        ChatRoomListResponse response = TestChatRoomFactory.getChatRoomListResponse();
        given(memberService.getMember(1L)).willReturn(member);
        given(chatRoomService.getChatRoomList(member, page)).willReturn(response);
        // when
        final ResultActions actions =
            mockMvc.perform(
                get("/chat-rooms")
                    .queryParam("page", page.toString()));
        // then
        actions.andExpect(status().isOk())
            .andExpect(jsonPath("$.data.elementSize").value("3"));
        actions.andDo(print())
            .andDo(document("chat-room-list", getDocumentRequest(), getDocumentResponse()));
    }
}