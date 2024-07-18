package com.amorgakco.backend.geospatial.service.mapper;

import com.amorgakco.backend.geospatial.dto.GroupGeospatial;

import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;

@Component
public class GeospatialMapper {

    public GroupGeospatial toGroupGeoSpatial(final Point point, final String groupId) {
        return GroupGeospatial.builder()
                .longitude(point.getX())
                .latitude(point.getY())
                .groupId(groupId)
                .build();
    }
}
