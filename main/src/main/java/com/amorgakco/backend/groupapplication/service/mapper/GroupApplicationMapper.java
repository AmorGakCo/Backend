package com.amorgakco.backend.groupapplication.service.mapper;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.groupapplication.domain.GroupApplication;
import com.amorgakco.backend.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class GroupApplicationMapper {

    public GroupApplication toGroupApplication(final Group group, final Member newParticipant) {
        return GroupApplication.builder().group(group).member(newParticipant).build();
    }
}
