package com.amorgakco.backend.groupjoining.domain;

import com.amorgakco.backend.global.BaseTime;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.domain.Participant;
import com.amorgakco.backend.member.domain.Member;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupJoining extends BaseTime {

    @Id @GeneratedValue private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @Enumerated(EnumType.STRING)
    private JoiningStatus joiningStatus;

    @Builder
    public GroupJoining(final Group group, final Member member) {
        this.group = group;
        this.member = member;
        this.joiningStatus = JoiningStatus.PENDING;
    }

    public void approve() {
        final Participant participant = new Participant(member);
        group.addParticipants(participant);
        this.joiningStatus = JoiningStatus.APPROVED;
    }
}
