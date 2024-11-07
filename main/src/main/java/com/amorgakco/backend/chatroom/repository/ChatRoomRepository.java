package com.amorgakco.backend.chatroom.repository;

import com.amorgakco.backend.chatroom.domain.ChatRoom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select cr from ChatRoom cr join fetch cr.group where cr.id =:chatRoomId")
    Optional<ChatRoom> findByIdWithGroup(@Param("chatRoomId") Long chatRoomId);
}
