package com.amorgakco.backend.group.domain;

import com.amorgakco.backend.global.BaseTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group extends BaseTime {

    @Id @GeneratedValue private Long id;

    private String name;
    private String description;
    private int memberCapacity;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Point point;
    private ProgressStatus progressStatus;

    public Group(
            final String name,
            final String description,
            final int memberCapacity,
            final LocalDateTime beginTime,
            final LocalDateTime endTime,
            final Point point) {
        this.name = name;
        this.description = description;
        this.memberCapacity = memberCapacity;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.point = point;
        this.progressStatus = ProgressStatus.BEFORE_START;
    }
}
