package com.amorgakco.backend.group.service;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.group.service.mapper.GroupMapper;
import com.amorgakco.backend.location.service.GroupLocationService;

import lombok.RequiredArgsConstructor;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final GeometryFactory geometryFactory;
    private final GeoOperations<String, String> geoOperations;
    private final GroupLocationService groupLocationService;

    public void register(final GroupRegisterRequest groupRegisterRequest) {
        final Point point = createPoint(groupRegisterRequest);
        final Group group = groupMapper.toGroup(groupRegisterRequest, point);
        final Long groupId = groupRepository.save(group).getId();
        groupLocationService.save(
                groupId, groupRegisterRequest.latitude(), groupRegisterRequest.longitude());
    }

    private Point createPoint(final GroupRegisterRequest groupRegisterRequest) {
        return geometryFactory.createPoint(
                new Coordinate(groupRegisterRequest.longitude(), groupRegisterRequest.latitude()));
    }
}
