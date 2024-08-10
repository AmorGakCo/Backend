package com.amorgakco.backend.groupparticipation.service.mapper;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.groupparticipation.domain.GroupParticipation;
import com.amorgakco.backend.member.domain.Member;

import org.springframework.stereotype.Component;

@Component
public class GroupParticipationMapper {

    public GroupParticipation toGroupParticipation(final Group group, final Member newParticipant) {
        return GroupParticipation.builder().group(group).member(newParticipant).build();
    }
}
