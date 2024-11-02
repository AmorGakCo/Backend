package com.amorgakco.backend.chatroomparticipant.service;

import com.amorgakco.backend.chatroomparticipant.repository.ChatRoomParticipantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomParticipantService {

    private final ChatRoomParticipantRepository chatRoomParticipantRepository;

}
