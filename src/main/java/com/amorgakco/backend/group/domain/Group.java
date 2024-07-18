package com.amorgakco.backend.group.domain;

import com.amorgakco.backend.global.BaseTime;
import com.amorgakco.backend.member.domain.Member;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

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

    @Column(columnDefinition = "geometry(POINT, 4326)")
    private Point location;

    @OneToOne(fetch = FetchType.LAZY)
    private Member host;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Participants> participants = new ArrayList<>();

    @Builder
    public Group(
            final String name,
            final String description,
            final int groupCapacity,
            final Duration duration,
            final Point location,
            final Member host,
            final String address) {
        this.name = name;
        this.description = description;
        this.groupCapacity = groupCapacity;
        this.duration = duration;
        this.location = location;
        this.host = host;
        this.address = address;
    }

    public void addParticipants(final Participants participants) {
        this.participants.add(participants);
        participants.add(this);
    }

    public int getCurrentGroupSize() {
        return participants.size() + 1;
    }
}
