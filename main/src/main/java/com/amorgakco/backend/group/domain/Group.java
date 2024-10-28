package com.amorgakco.backend.group.domain;

import com.amorgakco.backend.global.BaseTime;
import com.amorgakco.backend.global.exception.GroupAuthorityException;
import com.amorgakco.backend.global.exception.GroupCapacityException;
import com.amorgakco.backend.global.exception.LocationVerificationException;
import com.amorgakco.backend.global.exception.ParticipantException;
import com.amorgakco.backend.group.domain.location.Location;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.participant.domain.Participant;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "amorgakco_group",
        indexes = @Index(name = "idx_cell_token", columnList = "cell_token"))
public class Group extends BaseTime {

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private final Set<Participant> participants = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private int groupCapacity;
    private String address;
    @Embedded
    private Duration duration;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member host;

    @Embedded
    private Location location;

    @Builder
    public Group(
            final String name,
            final String description,
            final int groupCapacity,
            final LocalDateTime beginAt,
            final LocalDateTime endAt,
            final double longitude,
            final double latitude,
            final Member host,
            final String address) {
        this.name = name;
        this.description = description;
        this.groupCapacity = groupCapacity;
        this.duration = new Duration(beginAt, endAt);
        this.location = new Location(longitude, latitude);
        addParticipants(new Participant(host));
        this.host = host;
        this.address = address;
    }

    public boolean isMemberParticipated(final Long memberId){
        return participants.stream().anyMatch(p->p.isParticipant(memberId));
    }

    public void addParticipants(final Participant newParticipant) {
        validateParticipation(newParticipant);
        this.participants.add(newParticipant);
        newParticipant.add(this);
    }

    public void validateParticipation(final Participant participant) {
        validateDuplicatedParticipant(participant);
        validateGroupCapacity();
    }

    private void validateDuplicatedParticipant(final Participant participant) {
        if (participants.contains(participant)) {
            throw ParticipantException.duplicatedParticipant();
        }
    }

    private void validateGroupCapacity() {
        if (groupCapacity==getCurrentGroupSize()) {
            throw GroupCapacityException.exceedGroupCapacity();
        }
    }

    public int getCurrentGroupSize() {
        return participants.size();
    }

    public boolean isNotGroupHost(final Long memberId) {
        return !host.isEquals(memberId);
    }

    public boolean isGroupHost(final Long memberId){
        return host.isEquals(memberId);
    }

    public void verifyLocation(final double longitude, final double latitude) {
        if (location.isNotInBoundary(longitude, latitude)) {
            throw LocationVerificationException.verificationFailed();
        }
    }

    public boolean isInactivatedGroup() {
        return duration.getEndAt().isAfter(LocalDateTime.now());
    }

    public boolean isActivatedGroup() {
        return duration.getEndAt().isBefore(LocalDateTime.now());
    }
}
