package com.amorgakco.backend.group.service.mapper;

import com.amorgakco.backend.group.domain.Duration;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.domain.location.Location;
import com.amorgakco.backend.group.dto.GroupBasicResponse;
import com.amorgakco.backend.group.dto.GroupLocation;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.member.domain.Member;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper {

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
