package com.amorgakco.backend.participationrequest.domain;

import com.amorgakco.backend.global.BaseTime;
import com.amorgakco.backend.global.exception.IllegalAccessException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.participant.domain.Participant;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
public class ParticipationRequest extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member participant;

    @Enumerated(EnumType.STRING)
    private ParticipationStatus participationStatus;

    @Builder
    public ParticipationRequest(final Group group, final Member member) {
        this.group = group;
        this.participant = member;
        this.participationStatus = ParticipationStatus.PENDING;
    }

    public void approve(final Member host) {
        if (group.isNotGroupHost(host.getId())) {
            throw IllegalAccessException.noAuthorityForGroup();
        }
        participationStatus = ParticipationStatus.APPROVED;
        group.addParticipants(new Participant(participant));
    }

    public void reject(final Member host) {
        if (group.isNotGroupHost(host.getId())) {
            throw IllegalAccessException.noAuthorityForGroup();
        }
        participationStatus = ParticipationStatus.REJECTED;
    }
}
