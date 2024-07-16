package com.amorgakco.backend.group.service.mapper;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.dto.GroupBasicInfoResponse;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.member.domain.Member;

import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

@Component
public class GroupMapper {

    public Group toGroup(
            final Member host,
            final GroupRegisterRequest groupRegisterRequest,
            final Point location) {
        return Group.builder()
                .name(groupRegisterRequest.name())
                .address(groupRegisterRequest.address())
                .description(groupRegisterRequest.description())
                .beginTime(groupRegisterRequest.beginTime())
                .endTime(groupRegisterRequest.endTime())
                .groupCapacity(groupRegisterRequest.groupCapacity())
                .location(location)
                .host(host)
                .build();
    }

    public GroupBasicInfoResponse toGroupBasicInfoResponse(final Group group) {
        final Member host = group.getHost();
        return GroupBasicInfoResponse.builder()
                .hostNickname(host.getNickname())
                .hostPoint(host.getPoint())
                .hostGitHubUrl(host.getGitHubUrl())
                .hostImgUrl(host.getImgUrl())
                .address(group.getAddress())
                .groupCapacity(group.getGroupCapacity())
                .currentParticipants(group.getCurrentGroupSize())
                .beginTime(group.getBeginTime())
                .endTime(group.getEndTime())
                .build();
    }
}
