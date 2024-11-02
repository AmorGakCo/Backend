package com.amorgakco.backend.chatroomparticipant.domain;

import com.amorgakco.backend.chatroom.domain.ChatRoom;
import com.amorgakco.backend.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoomParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ChatRoom chatRoom;

    public ChatRoomParticipant(final Member member, final ChatRoom chatRoom) {
        this.member = member;
        this.chatRoom = chatRoom;
    }

    public String getNickname(){
        return member.getNickname();
    }

    public String getImgUrl(){
        return member.getImgUrl();
    }

    @Override
    public boolean equals(final Object o) {
        if (this==o) return true;
        if (o==null || getClass()!=o.getClass()) return false;
        final ChatRoomParticipant that = (ChatRoomParticipant) o;
        return Objects.equals(getMember(), that.getMember()) && Objects.equals(getChatRoom(), that.getChatRoom());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMember(), getChatRoom());
    }
}
