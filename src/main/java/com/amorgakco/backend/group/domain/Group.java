package com.amorgakco.backend.group.domain;

import com.amorgakco.backend.global.BaseTime;
import com.amorgakco.backend.global.exception.IllegalAccessException;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.domain.location.Location;
import com.amorgakco.backend.member.domain.Member;

import jakarta.persistence.*;

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
@Table(name = "groups")
public class Group extends BaseTime {

    @Id @GeneratedValue private Long id;
    private String name;
    private String description;
    private int groupCapacity;
    private String address;
    @Embedded private Duration duration;

    @OneToOne(fetch = FetchType.LAZY)
    private Member host;

    @Embedded private Location location;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private final Set<Participant> participants = new HashSet<>();

    @Builder
    public Group(
            final String name,
            final String description,
            final int groupCapacity,
            final LocalDateTime beginAt,
            final LocalDateTime endAt,
            final Location location,
            final Member host,
            final String address) {
        this.name = name;
        this.description = description;
        this.groupCapacity = groupCapacity;
        this.duration = new Duration(beginAt, endAt);
        this.location = location;
        this.host = host;
        this.address = address;
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
            throw IllegalAccessException.duplicatedParticipant();
        }
    }

    private void validateGroupCapacity() {
        if (groupCapacity == getCurrentGroupSize()) {
            throw IllegalAccessException.exceedGroupCapacity();
        }
    }

    public int getCurrentGroupSize() {
        return participants.size() + 1;
    }

    public boolean isNotGroupHost(final Long hostId) {
        return !host.isEquals(hostId);
    }

    public void verifyLocation(final double longitude, final double latitude, final Long memberId) {
        final Participant participant =
                participants.stream()
                        .filter(p -> p.isParticipant(memberId))
                        .findFirst()
                        .orElseThrow(ResourceNotFoundException::participantsNotFound);
        if (participant.isVerified()) {
            throw IllegalAccessException.verificationDuplicated();
        }
        if (location.isNotInBoundary(longitude, latitude)) {
            throw IllegalAccessException.verificationFailed();
        }
        participant.verify();
    }
}
