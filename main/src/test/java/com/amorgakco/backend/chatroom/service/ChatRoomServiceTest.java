package com.amorgakco.backend.chatroom.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.amorgakco.backend.chatroom.domain.ChatRoom;
import com.amorgakco.backend.chatroom.dto.ChatRoomResponse;
import com.amorgakco.backend.chatroom.repository.ChatRoomRepository;
import com.amorgakco.backend.chatroomparticipant.domain.ChatRoomParticipant;
import com.amorgakco.backend.chatroomparticipant.repository.ChatRoomParticipantRepository;
import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.groupparticipant.domain.GroupParticipant;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChatRoomServiceTest {

    @Autowired
    ChatRoomService chatRoomService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;
    @Autowired
    ChatRoomParticipantRepository chatRoomParticipantRepository;

    @Test
    @DisplayName("호스트는 그룹에 1:1 대응되는 채팅방을 등록할 수 있다.")
    void registerChatRoom() {
        //given
        Member host = TestMemberFactory.createEntity();
        memberRepository.save(host);
        Group group = TestGroupFactory.createActiveGroup(host);
        groupRepository.save(group);
        //when
        Long chatRoomId = chatRoomService.registerChatRoom(host, group);
        //then
        ChatRoom chatRoom = chatRoomRepository.findByIdWithGroup(chatRoomId).get();
        assertThat(chatRoom.getGroup().getId()).isEqualTo(group.getId());
    }

    @Test
    @DisplayName("그룹 참여자는 채팅방에 참여하면 채팅 참여자로 등록될 수 있다.")
    void participateChatRoom() {
        // given
        Member host = TestMemberFactory.createEntity();
        Member member = TestMemberFactory.createEntity();
        memberRepository.save(host);
        memberRepository.save(member);
        Group group = TestGroupFactory.createActiveGroup(host);
        group.addParticipant(new GroupParticipant(member));
        groupRepository.save(group);
        Long chatRoomId = chatRoomService.registerChatRoom(host, group);
        // when
        chatRoomService.participateChatRoom(member, chatRoomId);
        // then
        ChatRoomParticipant chatRoomParticipant =
            chatRoomParticipantRepository.findByMemberAndChatRoomId(
            member, chatRoomId).get();
        assertThat(chatRoomParticipant.getMember().getId()).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("자신이 참여중인 채팅방의 정보를 조회할 수 있다.")
    void getChatRoom() {
        // given
        Member host = TestMemberFactory.createEntity();
        Member member = TestMemberFactory.createEntity();
        memberRepository.save(host);
        memberRepository.save(member);
        Group group = TestGroupFactory.createActiveGroup(host);
        group.addParticipant(new GroupParticipant(member));
        groupRepository.save(group);
        Long chatRoomId = chatRoomService.registerChatRoom(host, group);
        chatRoomService.participateChatRoom(member, chatRoomId);
        // when
        ChatRoomResponse chatRoom = chatRoomService.getChatRoom(member, chatRoomId);
        // then
        assertThat(chatRoom.chatRoomId()).isEqualTo(chatRoomId);
    }

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
        Long chatRoomId = chatRoomService.registerChatRoom(host, group);
        chatRoomService.participateChatRoom(member, chatRoomId);
        // when
        chatRoomService.exitChatRoom(member, chatRoomId);
        // then
        Optional<ChatRoomParticipant> chatRoomParticipant =
            chatRoomParticipantRepository.findByMemberAndChatRoomId(
            member, chatRoomId);
        assertThat(chatRoomParticipant).isNotPresent();
    }
}