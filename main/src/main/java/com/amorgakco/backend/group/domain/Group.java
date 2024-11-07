package com.amorgakco.backend.group.domain;

import com.amorgakco.backend.global.BaseTime;
import com.amorgakco.backend.global.exception.DuplicatedRequestException;
import com.amorgakco.backend.global.exception.GroupAuthorityException;
import com.amorgakco.backend.global.exception.GroupCapacityException;
import com.amorgakco.backend.global.exception.LocationVerificationException;
import com.amorgakco.backend.group.domain.location.Location;
import com.amorgakco.backend.groupparticipant.domain.GroupParticipant;
import com.amorgakco.backend.member.domain.Member;
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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
    name = "amorgakco_group",
    indexes = @Index(name = "idx_cell_token", columnList = "cell_token"))
public class Group extends BaseTime {

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<GroupParticipant> groupParticipants = new HashSet<>();

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
        addParticipant(new GroupParticipant(host));
        this.host = host;
        this.address = address;
    }

    public void addParticipant(final GroupParticipant newGroupParticipant) {
        validateParticipation(newGroupParticipant);
        this.groupParticipants.add(newGroupParticipant);
        newGroupParticipant.add(this);
    }

    public void validateParticipation(final GroupParticipant groupParticipant) {
        validateDuplicatedParticipant(groupParticipant);
        validateGroupCapacity();
    }

    private void validateDuplicatedParticipant(final GroupParticipant groupParticipant) {
        if (groupParticipants.contains(groupParticipant)) {
            throw DuplicatedRequestException.duplicatedParticipant();
        }
    }

    private void validateGroupCapacity() {
        if (groupCapacity == getCurrentGroupSize()) {
            throw GroupCapacityException.exceedGroupCapacity();
        }
    }

    public int getCurrentGroupSize() {
        return groupParticipants.size();
    }

    public boolean isMemberParticipated(final Long memberId) {
        return !groupParticipants.stream().anyMatch(p -> p.isParticipant(memberId));
    }

    public void validateGroupHost(final Member member) {
        if (!host.isEquals(member.getId())) {
            throw GroupAuthorityException.noAuthorityForGroup();
        }
    }

    public boolean isGroupHost(final Long memberId) {
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
