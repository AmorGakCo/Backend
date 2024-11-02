package com.amorgakco.backend.chatroom.controller;

import com.amorgakco.backend.chatroom.dto.ChatRoomResponse;
import com.amorgakco.backend.chatroom.dto.ChatRoomSliceResponse;
import com.amorgakco.backend.chatroom.service.ChatRoomService;
import com.amorgakco.backend.global.argumentresolver.AuthMember;
import com.amorgakco.backend.global.argumentresolver.AuthMemberId;
import com.amorgakco.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat-room")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping
    public ChatRoomSliceResponse getChatRoomList(@AuthMemberId final Member member, @RequestParam final Integer page) {
        return chatRoomService.getChatRoomList(member, page);
    }

    @PostMapping("/{chatRoomId}")
    public ChatRoomResponse enterChatRoom(@AuthMember final Member member, @PathVariable final Long chatRoomId) {
        return chatRoomService.enterChatRoom(member, chatRoomId);
    }

    @DeleteMapping("/{chatRoomId}")
    public void exitChatRoom(@AuthMember Member member, @PathVariable final Long chatRoomId) {
        chatRoomService.exitChatRoom(member, chatRoomId);
    }
}
