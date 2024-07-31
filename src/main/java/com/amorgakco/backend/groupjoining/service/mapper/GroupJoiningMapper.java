package com.amorgakco.backend.groupjoining.service.mapper;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.groupjoining.domain.GroupJoining;
import com.amorgakco.backend.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class GroupJoiningMapper {

    public GroupJoining toGroupJoining(final Group group, final Member newParticipant) {
        return GroupJoining.builder().group(group).member(newParticipant).build();
    }
}
