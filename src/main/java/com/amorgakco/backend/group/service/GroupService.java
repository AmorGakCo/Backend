package com.amorgakco.backend.group.service;

import com.amorgakco.backend.global.CommonIdResponse;
import com.amorgakco.backend.global.exception.IllegalAccessException;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.domain.Duration;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.domain.Participants;
import com.amorgakco.backend.group.dto.GroupBasicInfoResponse;
import com.amorgakco.backend.group.dto.GroupLocation;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.group.dto.GroupSearchResponse;
import com.amorgakco.backend.group.dto.LocationVerificationRequest;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.group.service.mapper.GroupMapper;
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
@Transactional(readOnly = true)
public class GroupService {

    private static final double VERIFICATION_RADIUS_LIMIT = 0.15;
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final GeometryFactory geometryFactory;
    private final GeoCalculator geoCalculator;
    private final MemberService memberService;

    @Transactional
    public CommonIdResponse register(final GroupRegisterRequest request, final Long hostId) {
        final Point location = createLocation(request);
        final Member host = memberService.getMember(hostId);
        final Duration duration = new Duration(request.beginAt(), request.endAt());
        final Group group = groupMapper.toGroup(host, request, location, duration);
        final Long groupId = groupRepository.save(group).getId();
        geoCalculator.save(groupId.toString(), request.longitude(), request.latitude());
        return new CommonIdResponse(groupId);
    }

    private Point createLocation(final GroupRegisterRequest groupRegisterRequest) {
        return geometryFactory.createPoint(
                new Coordinate(groupRegisterRequest.longitude(), groupRegisterRequest.latitude()));
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

    public GroupBasicInfoResponse getBasicGroupInfo(final Long groupId) {
        final Group group =
                groupRepository
                        .findById(groupId)
                        .orElseThrow(ResourceNotFoundException::groupNotFound);
        return groupMapper.toGroupBasicInfoResponse(group);
    }

    public GroupSearchResponse getNearByGroups(
            final double width,
            final double height,
            final double longitude,
            final double latitude) {
        return new GroupSearchResponse(
                geoCalculator.searchByBox(width, height, longitude, latitude));
    }

    public void verifyParticipantLocation(
            final LocationVerificationRequest request, final Long memberId) {
        final GroupLocation groupLocation =
                geoCalculator
                        .searchByCircle(
                                VERIFICATION_RADIUS_LIMIT, request.longitude(), request.latitude())
                        .stream()
                        .filter(g -> g.groupId().equals(request.groupId()))
                        .findFirst()
                        .orElseThrow(IllegalAccessException::verificationFailed);
        final Group group = getGroup(Long.parseLong(groupLocation.groupId()));
        group.verifyLocation(memberId);
    }

    @Transactional
    public void approve(final Long memberId, final Long groupId) {
        final Group group = getGroup(groupId);
        final Member member = memberService.getMember(memberId);
        final Participants participants = new Participants(member);
        group.addParticipants(participants);
    }
}
