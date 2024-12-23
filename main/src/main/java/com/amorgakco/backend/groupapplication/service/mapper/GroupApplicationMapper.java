package com.amorgakco.backend.groupapplication.service.mapper;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.groupapplication.domain.GroupApplication;
import com.amorgakco.backend.groupapplication.dto.ApplicationListResponse;
import com.amorgakco.backend.groupapplication.dto.ApplicationResponse;
import com.amorgakco.backend.member.domain.Member;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class GroupApplicationMapper {

    public GroupApplication toGroupApplication(final Group group, final Member newParticipant) {
        return GroupApplication.builder().group(group).member(newParticipant).build();
    }

    public ApplicationListResponse toApplicationListResponse(
        final List<GroupApplication> groupApplications) {
        return new ApplicationListResponse(
            groupApplications.stream().map(this::toApplicationResponse).toList());
    }

    private ApplicationResponse toApplicationResponse(final GroupApplication groupApplication) {
        final Group group = groupApplication.getGroup();
        final Member member = groupApplication.getApplicant();
        return ApplicationResponse.builder()
            .groupId(group.getId())
            .memberId(member.getId())
            .groupName(group.getName())
            .applicationMemberNickname(member.getNickname())
            .build();
    }
}
