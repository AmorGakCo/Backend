package com.amorgakco.backend.group.service.mapper;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;

import org.locationtech.jts.geom.Point;
import org.mapstruct.Mapper;

@Mapper
public interface GroupMapper {
    Group toGroup(GroupRegisterRequest groupRegisterRequest, Point location);
}
