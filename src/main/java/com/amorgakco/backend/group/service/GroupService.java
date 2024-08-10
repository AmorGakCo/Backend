package com.amorgakco.backend.group.service;

import com.amorgakco.backend.global.IdResponse;
import com.amorgakco.backend.global.exception.IllegalAccessException;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.domain.location.Location;
import com.amorgakco.backend.group.dto.GroupBasicResponse;
import com.amorgakco.backend.group.dto.GroupDetailResponse;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.group.dto.GroupSearchResponse;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.group.service.mapper.GroupMapper;
import com.amorgakco.backend.member.domain.Member;

import lombok.RequiredArgsConstructor;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final GeometryFactory geometryFactory;

    @Transactional
    public IdResponse register(final GroupRegisterRequest request, final Member host) {
        final Location location = createLocation(request.longitude(), request.latitude());
        final Group group = groupMapper.toGroup(host, request, location);
        final Long groupId = groupRepository.save(group).getId();
        return new IdResponse(groupId);
    }

    private Location createLocation(final double longitude, final double latitude) {
        return new Location(geometryFactory.createPoint(new Coordinate(longitude, latitude)));
    }

    @Transactional
    public void delete(final Long hostId, final Long groupId) {
        final Group group = getGroup(groupId);
        if (group.isNotGroupHost(hostId)) {
            throw IllegalAccessException.noAuthorityForGroup();
        }
        groupRepository.delete(group);
    }

    public Group getGroup(final Long groupId) {
        return groupRepository
                .findById(groupId)
                .orElseThrow(ResourceNotFoundException::groupNotFound);
    }

    public GroupDetailResponse getDetailGroup(final Long groupId) {
        final Group group = getGroup(groupId);
        return groupMapper.toGroupDetailResponse(group);
    }

    public GroupBasicResponse getBasicGroup(final Long groupId) {
        final Group group =
                groupRepository
                        .findByIdWithHost(groupId)
                        .orElseThrow(ResourceNotFoundException::groupNotFound);
        return groupMapper.toGroupBasicInfoResponse(group);
    }

    public GroupSearchResponse getNearByGroups(
            final double longitude, final double latitude, final double radius) {
        final Location location = createLocation(longitude, latitude);
        final double validRadius = location.validateAndGetRadius(radius);
        final Point point = location.getPoint();
        return groupRepository
                .findByLocationWithRadius(point.getX(), point.getY(), validRadius)
                .stream()
                .map(groupMapper::toGroupLocation)
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toList(), GroupSearchResponse::new));
    }
}
