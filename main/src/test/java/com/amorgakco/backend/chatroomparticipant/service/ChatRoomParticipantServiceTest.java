package com.amorgakco.backend.chatroomparticipant.service;

import com.amorgakco.backend.chatroom.repository.ChatRoomRepository;
import com.amorgakco.backend.chatroom.service.ChatRoomService;
import com.amorgakco.backend.chatroomparticipant.domain.ChatRoomParticipant;
import com.amorgakco.backend.chatroomparticipant.repository.ChatRoomParticipantRepository;
import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.groupparticipant.domain.GroupParticipant;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChatRoomParticipantServiceTest {

    @Autowired
    ChatRoomParticipantService chatRoomParticipantService;
    @Autowired
    ChatRoomService chatRoomService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    ChatRoomParticipantRepository chatRoomParticipantRepository;

    @Test
    @DisplayName("채팅방 참여자는 채팅방을 나갈 수 있다.")
    void exitChatRoom() {
        // given
        Member host = TestMemberFactory.createEntity();
        Member member = TestMemberFactory.createEntity();
        memberRepository.save(host);
        memberRepository.save(member);
        Group group = TestGroupFactory.createActiveGroup(host);
        group.addParticipant(new GroupParticipant(member));
        groupRepository.save(group);
        Long chatRoomId = chatRoomService.registerChatRoom(host,group);
        chatRoomService.participateChatRoom(member,chatRoomId);
        // when
        chatRoomParticipantService.exitChatRoom(member,chatRoomId);
        // then
        Optional<ChatRoomParticipant> chatRoomParticipant = chatRoomParticipantRepository.findByMemberAndChatRoomId(member, chatRoomId);
        assertThat(chatRoomParticipant).isNotPresent();
    }


}