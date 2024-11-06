package com.amorgakco.backend.chatroomparticipant.repository;

import com.amorgakco.backend.chatroom.domain.ChatRoom;
import com.amorgakco.backend.chatroomparticipant.domain.ChatRoomParticipant;
import com.amorgakco.backend.member.domain.Member;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomParticipantRepository extends JpaRepository<ChatRoomParticipant,Long> {

    @Query("select crp from ChatRoomParticipant crp join fetch crp.member where crp.member =:member and crp.chatRoom.id=:chatRoomId")
    Optional<ChatRoomParticipant> findByMemberAndChatRoomId(final Member member, final Long chatRoomId);

    @Query("select crp from ChatRoomParticipant crp join fetch crp.chatRoom where crp.member = :member")
    Slice<ChatRoom> findAllByMember(Member member, PageRequest pageRequest);
}
