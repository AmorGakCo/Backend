package com.amorgakco.backend.group.service;

import com.amorgakco.backend.global.exception.ErrorCode;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.domain.Participants;
import com.amorgakco.backend.group.dto.GroupBasicInfoResponse;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.group.dto.GroupRegisterResponse;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.group.service.mapper.GroupMapper;
import com.amorgakco.backend.location.service.GeoSpatialService;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.service.MemberService;

import lombok.RequiredArgsConstructor;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final GeometryFactory geometryFactory;
    private final GeoSpatialService locationService;
    private final MemberService memberService;

    public GroupRegisterResponse register(
            final GroupRegisterRequest groupRegisterRequest, final Long hostId) {
        final Point location = createLocation(groupRegisterRequest);
        final Member host = memberService.getMember(hostId);
        final Participants participants = new Participants(host);
        final Group group = groupMapper.toGroup(host, groupRegisterRequest, location, participants);
        final Long groupId = groupRepository.save(group).getId();
        locationService.save(
                groupId, groupRegisterRequest.latitude(), groupRegisterRequest.longitude());
        return new GroupRegisterResponse(groupId);
    }

    private Point createLocation(final GroupRegisterRequest groupRegisterRequest) {
        return geometryFactory.createPoint(
                new Coordinate(groupRegisterRequest.longitude(), groupRegisterRequest.latitude()));
    }

    @Transactional(readOnly = true)
    public GroupBasicInfoResponse getGroupInfo(final Long groupId) {
        final Group group =
                groupRepository
                        .findById(groupId)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(ErrorCode.GROUP_NOT_FOUND));
        return groupMapper.toGroupBasicInfoResponse(group);
    }
}
