package com.amorgakco.backend.group.domain;

import com.amorgakco.backend.member.domain.Member;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participants {

    @Id @GeneratedValue private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Group group;

    @Enumerated(EnumType.STRING)
    private LocationVerifyStatus locationVerifyStatus;

    public Participants(final Member member) {
        this.member = member;
        this.locationVerifyStatus = LocationVerifyStatus.UNVERIFIED;
    }

    public void locationVerified() {
        this.locationVerifyStatus = LocationVerifyStatus.VERIFIED;
    }

    public void add(final Group group) {
        this.group = group;
    }
}