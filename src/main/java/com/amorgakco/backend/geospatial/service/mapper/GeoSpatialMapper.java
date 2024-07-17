package com.amorgakco.backend.geospatial.service.mapper;

import com.amorgakco.backend.geospatial.dto.GroupGeoSpatial;

import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;

@Component
public class GeoSpatialMapper {

    public GroupGeoSpatial toGroupGeoSpatial(final Point point, final String groupId) {
        return GroupGeoSpatial.builder()
                .longitude(point.getX())
                .latitude(point.getY())
                .groupId(groupId)
                .build();
    }
}
