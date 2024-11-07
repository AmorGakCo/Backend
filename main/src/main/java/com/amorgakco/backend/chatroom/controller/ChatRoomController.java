package com.amorgakco.backend.chatroom.controller;

import com.amorgakco.backend.chatroom.dto.ChatRoomListResponse;
import com.amorgakco.backend.chatroom.dto.ChatRoomResponse;
import com.amorgakco.backend.chatroom.service.ChatRoomService;
import com.amorgakco.backend.global.argumentresolver.AuthMember;
import com.amorgakco.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat-rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping
    public ChatRoomListResponse getChatRoomList(@AuthMember final Member member,
        @RequestParam final Integer page) {
        return chatRoomService.getChatRoomList(member, page);
    }

    @GetMapping("/{chatRoomId}")
    public ChatRoomResponse getChatRoom(@AuthMember final Member member,
        @PathVariable final Long chatRoomId) {
        return chatRoomService.getChatRoom(member, chatRoomId);
    }

    @PostMapping("/{chatRoomId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatRoomResponse participateChatRoom(@AuthMember final Member member,
        @PathVariable final Long chatRoomId) {
        return chatRoomService.participateChatRoom(member, chatRoomId);
    }

    @DeleteMapping("/{chatRoomId}")
    public void exitChatRoom(@AuthMember Member member, @PathVariable final Long chatRoomId) {
        chatRoomService.exitChatRoom(member, chatRoomId);
    }
}
