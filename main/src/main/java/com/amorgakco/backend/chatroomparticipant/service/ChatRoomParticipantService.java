package com.amorgakco.backend.chatroomparticipant.service;

import com.amorgakco.backend.chatroom.domain.ChatRoom;
import com.amorgakco.backend.chatroomparticipant.domain.ChatRoomParticipant;
import com.amorgakco.backend.chatroomparticipant.repository.ChatRoomParticipantRepository;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomParticipantService {

    private static final Integer PAGE_CONTENT_SIZE = 10;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;

    public Slice<ChatRoom> getParticipatedChatRooms(final Member member, final Integer page){
        PageRequest pageRequest = PageRequest
                .of(page, PAGE_CONTENT_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
        return chatRoomParticipantRepository.findByMemberWithChatRoom(member, pageRequest);
    }

    public ChatRoomParticipant getChatRoomParticipant(final Member member, final Long chatRoomId) {
        return chatRoomParticipantRepository
                .findByMemberAndChatRoomId(member, chatRoomId)
                .orElseThrow(ResourceNotFoundException::chatRoomParticipantsNotFound);
    }
}
