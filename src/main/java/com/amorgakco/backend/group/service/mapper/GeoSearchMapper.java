package com.amorgakco.backend.group.service.mapper;

import com.amorgakco.backend.group.dto.GroupLocation;

import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;

@Component
public class GeoSearchMapper {

    public GroupLocation toGroupLocation(final Point point, final String groupId) {
        return GroupLocation.builder()
                .longitude(point.getX())
                .latitude(point.getY())
                .groupId(groupId)
                .build();
    }
}
