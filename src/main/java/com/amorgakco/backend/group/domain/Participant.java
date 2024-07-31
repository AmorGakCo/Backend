package com.amorgakco.backend.group.domain;

import com.amorgakco.backend.member.domain.Member;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant {

    @Id @GeneratedValue private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Group group;

    @Enumerated(EnumType.STRING)
    private LocationVerificationStatus locationVerificationStatus;

    public Participant(final Member member) {
        this.member = member;
        this.locationVerificationStatus = LocationVerificationStatus.UNVERIFIED;
    }

    public boolean isVerified() {
        return this.locationVerificationStatus.equals(LocationVerificationStatus.VERIFIED);
    }

    public void verify() {
        this.locationVerificationStatus = LocationVerificationStatus.VERIFIED;
    }

    public boolean isParticipant(final Long memberId) {
        return member.isEquals(memberId);
    }

    public Long getMemberId() {
        return member.getId();
    }

    public void add(final Group group) {
        this.group = group;
    }
}
