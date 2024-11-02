package com.amorgakco.backend.chatroomparticipant.controller;

import com.amorgakco.backend.chatroom.domain.ChatRoom;
import com.amorgakco.backend.chatroomparticipant.domain.ChatRoomParticipant;
import com.amorgakco.backend.chatroomparticipant.service.ChatRoomParticipantService;
import com.amorgakco.backend.global.argumentresolver.AuthMember;
import com.amorgakco.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat-room-participant")
@RequiredArgsConstructor
public class ChatRoomParticipantController {

    private final ChatRoomParticipantService chatRoomParticipantService;

    @DeleteMapping
    public void exitChatRoom(@AuthMember Member member){
        chatRoomParticipantService.exitChatRoom(member);
    }
}
