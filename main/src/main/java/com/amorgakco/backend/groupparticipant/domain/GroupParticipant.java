package com.amorgakco.backend.groupparticipant.domain;

import com.amorgakco.backend.global.BaseTime;
import com.amorgakco.backend.global.exception.LocationVerificationException;
import com.amorgakco.backend.global.exception.ParticipantException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupParticipant extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Group group;

    @Enumerated(EnumType.STRING)
    private LocationVerificationStatus locationVerificationStatus;

    public GroupParticipant(final Member member) {
        this.member = member;
        this.locationVerificationStatus = LocationVerificationStatus.UNVERIFIED;
    }

    public boolean isParticipant(final Long memberId) {
        return member.isEquals(memberId);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GroupParticipant that = (GroupParticipant) o;
        return Objects.equals(getMember().getId(), that.getMember().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMember().getId());
    }

    public void verify(final double longitude, final double latitude) {
        if (isVerified()) {
            throw LocationVerificationException.verificationDuplicated();
        }
        group.verifyLocation(longitude, latitude);
        this.locationVerificationStatus = LocationVerificationStatus.VERIFIED;
    }

    private boolean isVerified() {
        return this.locationVerificationStatus.equals(LocationVerificationStatus.VERIFIED);
    }

    public void add(final Group group) {
        this.group = group;
    }

    public Integer increaseTemperature(final GroupParticipant requestGroupParticipant) {
        requestGroupParticipant.validateSameGroupParticipant(this);
        return member.increaseMoGakCoTemperature();
    }

    private void validateSameGroupParticipant(final GroupParticipant targetGroupParticipant) {
        if (!Objects.equals(this.group.getId(), targetGroupParticipant.getGroup().getId())) {
            throw ParticipantException.notSameGroupParticipant();
        }
    }

    public Integer decreaseTemperature(final GroupParticipant requestGroupParticipant) {
        requestGroupParticipant.validateSameGroupParticipant(this);
        return member.decreaseMoGakCoTemperature();
    }
}
