package com.amorgakco.backend.chatroomparticipant.service;

import com.amorgakco.backend.chatroomparticipant.domain.ChatRoomParticipant;
import com.amorgakco.backend.chatroomparticipant.repository.ChatRoomParticipantRepository;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.service.GroupService;
import com.amorgakco.backend.groupparticipant.domain.GroupParticipant;
import com.amorgakco.backend.groupparticipant.service.GroupParticipantService;
import com.amorgakco.backend.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomParticipantService {

    private final ChatRoomParticipantRepository chatRoomParticipantRepository;

    public void exitChatRoom(final Member member){
        final ChatRoomParticipant chatRoomParticipant = chatRoomParticipantRepository.findByMember(member)
                .orElseThrow(ResourceNotFoundException::memberNotFound);
        chatRoomParticipantRepository.delete(chatRoomParticipant);
    }
}
