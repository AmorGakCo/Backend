package com.amorgakco.backend.participationrequest.service;

import com.amorgakco.backend.global.exception.IllegalAccessException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.participant.domain.Participant;
import com.amorgakco.backend.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ParticipationRequestValidator {

    private static final Integer PARTICIPATION_LIMIT = 5;
    private final ParticipantRepository participantRepository;

    public void validate(final Group group, final Member member){
        Integer participationCount = participantRepository.countByParticipantMember(member);
        if(participationCount>PARTICIPATION_LIMIT){
            throw IllegalAccessException.exceedParticipationLimit();
        }
        group.validateParticipation(new Participant(member));
    }
}
