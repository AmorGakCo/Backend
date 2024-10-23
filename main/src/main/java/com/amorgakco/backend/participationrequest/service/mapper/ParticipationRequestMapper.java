package com.amorgakco.backend.participationrequest.service.mapper;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.participationrequest.domain.ParticipationRequest;
import com.amorgakco.backend.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class ParticipationRequestMapper {

    public ParticipationRequest toGroupParticipation(final Group group, final Member newParticipant) {
        return ParticipationRequest.builder().group(group).member(newParticipant).build();
    }
}
