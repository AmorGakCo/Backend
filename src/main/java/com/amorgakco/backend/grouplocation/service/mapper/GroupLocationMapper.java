package com.amorgakco.backend.grouplocation.service.mapper;

import com.amorgakco.backend.grouplocation.dto.GroupLocation;

import org.springframework.data.geo.Point;

public class GroupLocationMapper {

    public GroupLocation toGroupLocation(Point point, String groupId) {
        return GroupLocation.builder()
                .longitude(point.getX())
                .latitude(point.getY())
                .groupId(groupId)
                .build();
    }
}
