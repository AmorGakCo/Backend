package com.amorgakco.backend.chatroom.domain;


import com.amorgakco.backend.chatroomparticipant.domain.ChatRoomParticipant;
import com.amorgakco.backend.global.BaseTime;
import com.amorgakco.backend.global.exception.ParticipantException;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.member.domain.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatRoom extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ChatRoomParticipant> chatRoomParticipants = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    private Group group;

    public ChatRoom(final Member host, final Group group){
        this.group = group;
        participate(host);
    }

    public void participate(final Member member){
        if(isMemberNotParticipatedInGroup(member)){
            throw ParticipantException.notParticipatedInGroup();
        }
        ChatRoomParticipant chatRoomParticipant = new ChatRoomParticipant(member, this);
        chatRoomParticipants.add(chatRoomParticipant);
    }

    private boolean isMemberNotParticipatedInGroup(final Member member) {
        return group.isMemberParticipated(member.getId());
    }

    public void validateChatRoomParticipant(final Member member){
        final boolean isNotParticipated = chatRoomParticipants.stream()
                .noneMatch(cp -> cp.getMember().isEquals(member.getId()));
        if(isNotParticipated){
            throw ResourceNotFoundException.participationNotFound();
        }
    }
}
