package com.amorgakco.backend.group.service.mapper;

import com.amorgakco.backend.group.domain.Duration;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.domain.location.Location;
import com.amorgakco.backend.group.dto.GroupBasicResponse;
import com.amorgakco.backend.group.dto.GroupDetailResponse;
import com.amorgakco.backend.group.dto.GroupLocation;
import com.amorgakco.backend.group.dto.GroupMember;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.member.domain.Member;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupMapper {

    public GroupDetailResponse toGroupDetailResponse(final Group group) {
        final List<GroupMember> groupMembers =
                group.getParticipants().stream().map(p -> toGroupMember(p.getMember())).toList();
        return GroupDetailResponse.builder()
                .host(toGroupMember(group.getHost()))
                .name(group.getName())
                .address(group.getAddress())
                .description(group.getDescription())
                .beginAt(group.getDuration().getBeginAt())
                .endAt(group.getDuration().getEndAt())
                .latitude(group.getLocation().getPoint().getY())
                .longitude(group.getLocation().getPoint().getY())
                .groupMembers(groupMembers)
                .build();
    }

    public GroupMember toGroupMember(final Member member) {
        return GroupMember.builder()
                .githubUrl(member.getGithubUrl())
                .moGakCoTemperature(member.getMoGakCoTemperature())
                .nickname(member.getNickname())
                .imgUrl(member.getImgUrl())
                .build();
    }

    public Group toGroup(
            final Member host, final GroupRegisterRequest request, final Location location) {
        return Group.builder()
                .name(request.name())
                .address(request.address())
                .description(request.description())
                .beginAt(request.beginAt())
                .endAt(request.endAt())
                .groupCapacity(request.groupCapacity())
                .location(location)
                .host(host)
                .build();
    }

    public GroupBasicResponse toGroupBasicInfoResponse(final Group group) {
        final Member host = group.getHost();
        final Duration duration = group.getDuration();
        return GroupBasicResponse.builder()
                .hostNickname(host.getNickname())
                .hostImgUrl(host.getImgUrl())
                .address(group.getAddress())
                .groupCapacity(group.getGroupCapacity())
                .currentParticipants(group.getCurrentGroupSize())
                .beginAt(duration.getBeginAt())
                .endAt(duration.getEndAt())
                .build();
    }

    public GroupLocation toGroupLocation(final Group group) {
        final Point point = group.getLocation().getPoint();
        return GroupLocation.builder()
                .longitude(point.getX())
                .latitude(point.getY())
                .groupId(group.getId())
                .build();
    }
}
