package com.amorgakco.backend.chatroom.service;

import com.amorgakco.backend.chatroom.domain.ChatRoom;
import com.amorgakco.backend.chatroom.dto.ChatRoomResponse;
import com.amorgakco.backend.chatroom.dto.ChatRoomSliceResponse;
import com.amorgakco.backend.chatroom.repository.ChatRoomRepository;
import com.amorgakco.backend.chatroom.service.mapper.ChatRoomMapper;
import com.amorgakco.backend.chatroomparticipant.domain.ChatRoomParticipant;
import com.amorgakco.backend.chatroomparticipant.repository.ChatRoomParticipantRepository;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

    private static final Integer PAGE_CONTENT_SIZE = 10;
    private final ChatRoomParticipantRepository chatRoomParticipantRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper chatRoomMapper;

    public ChatRoomSliceResponse getChatRoomList(final Member member, final Integer page) {
        PageRequest pageRequest = PageRequest
                .of(page, PAGE_CONTENT_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
        Slice<ChatRoom> chatRooms = chatRoomParticipantRepository.findAllByMember(member, pageRequest);
        return chatRoomMapper.toChatRoomSliceResponse(chatRooms);
    }

    public ChatRoomResponse enterChatRoom(final Member member, final Long chatRoomId){
        final ChatRoom chatRoom = chatRoomRepository
                .findById(chatRoomId)
                .orElseThrow(ResourceNotFoundException::chatRoomNotFound);
        chatRoom.enterChatRoom(member);
        return chatRoomMapper.toChatRoomResponse(chatRoom);
    }

    public void exitChatRoom(final Member member,final Long chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(ResourceNotFoundException::chatRoomNotFound);
        final ChatRoomParticipant chatRoomParticipant = chatRoomParticipantRepository.findByMemberAndChatRoom(member,chatRoom)
                .orElseThrow(ResourceNotFoundException::memberNotFound);
        chatRoomParticipantRepository.delete(chatRoomParticipant);
    }
}
