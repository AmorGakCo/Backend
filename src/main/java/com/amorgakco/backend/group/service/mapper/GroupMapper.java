package com.amorgakco.backend.group.service.mapper;

import com.amorgakco.backend.group.domain.Duration;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.dto.GroupBasicResponse;
import com.amorgakco.backend.group.dto.GroupDetailResponse;
import com.amorgakco.backend.group.dto.GroupLocation;
import com.amorgakco.backend.group.dto.GroupMember;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.member.domain.Member;

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
                .latitude(group.getLocation().getLatitude())
                .longitude(group.getLocation().getLongitude())
                .groupMembers(groupMembers)
                .build();
    }

    public GroupMember toGroupMember(final Member member) {
        return GroupMember.builder()
                .memberId(member.getId())
                .githubUrl(member.getGithubUrl())
                .moGakCoTemperature(member.getMoGakCoTemperature())
                .nickname(member.getNickname())
                .imgUrl(member.getImgUrl())
                .build();
    }

    public Group toGroup(final Member host, final GroupRegisterRequest request) {
        return Group.builder()
                .name(request.name())
                .address(request.address())
                .description(request.description())
                .beginAt(request.beginAt())
                .endAt(request.endAt())
                .groupCapacity(request.groupCapacity())
                .longitude(request.longitude())
                .latitude(request.latitude())
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
        return GroupLocation.builder()
                .longitude(group.getLocation().getLongitude())
                .latitude(group.getLocation().getLatitude())
                .groupId(group.getId())
                .build();
    }
}
