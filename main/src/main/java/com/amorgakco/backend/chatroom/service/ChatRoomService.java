package com.amorgakco.backend.chatroom.service;

import com.amorgakco.backend.chatroom.domain.ChatRoom;
import com.amorgakco.backend.chatroom.dto.ChatRoomListResponse;
import com.amorgakco.backend.chatroom.dto.ChatRoomResponse;
import com.amorgakco.backend.chatroom.repository.ChatRoomRepository;
import com.amorgakco.backend.chatroom.service.mapper.ChatRoomMapper;
import com.amorgakco.backend.chatroomparticipant.domain.ChatRoomParticipant;
import com.amorgakco.backend.chatroomparticipant.service.ChatRoomParticipantService;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomService {

    private final ChatRoomParticipantService chatRoomParticipantService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper chatRoomMapper;

    public ChatRoomListResponse getChatRoomList(final Member member, final Integer page) {
        Slice<ChatRoom> participatedChatRooms = chatRoomParticipantService.getParticipatedChatRooms(member, page);
        return chatRoomMapper.toChatRoomListResponse(participatedChatRooms);
    }

    public ChatRoomResponse getChatRoom(final Member member, final Long chatRoomId) {
        final ChatRoom chatRoom = getChatRoomByChatRoomId(chatRoomId);
        chatRoom.validateChatRoomParticipant(member);
        return chatRoomMapper.toChatRoomResponse(chatRoom);
    }

    private ChatRoom getChatRoomByChatRoomId(final Long chatRoomId) {
        return chatRoomRepository
                .findByIdWithGroup(chatRoomId)
                .orElseThrow(ResourceNotFoundException::chatRoomNotFound);
    }

    @Transactional
    public ChatRoomResponse participateChatRoom(final Member member, final Long chatRoomId){
        final ChatRoom chatRoom = getChatRoomByChatRoomId(chatRoomId);
        chatRoom.participate(member);
        return chatRoomMapper.toChatRoomResponse(chatRoom);
    }

    @Transactional
    public Long registerChatRoom(final Member member,final Group group){
        ChatRoom newChatRoom = new ChatRoom(member,group);
        return chatRoomRepository.save(newChatRoom).getId();
    }

    public void exitChatRoom(final Member member, final Long chatRoomId){
        ChatRoomParticipant chatRoomParticipant = chatRoomParticipantService
                .getChatRoomParticipant(member, chatRoomId);
        chatRoomParticipant.exitChatRoom();
    }
}
