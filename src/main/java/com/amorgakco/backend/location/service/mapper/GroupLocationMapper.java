package com.amorgakco.backend.location.service.mapper;

import com.amorgakco.backend.location.dto.GroupLocation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.geo.Point;

@Mapper
public interface GroupLocationMapper {

    @Mapping(source = "point.x", target = "longitude")
    @Mapping(source = "point.y", target = "latitude")
    GroupLocation toGroupLocation(Point point, String groupId);
}
