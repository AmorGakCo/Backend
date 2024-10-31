package com.amorgakco.backend.groupapplication.service;

import com.amorgakco.backend.global.exception.DuplicatedRequestException;
import com.amorgakco.backend.global.exception.ParticipantException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.groupapplication.repository.GroupApplicationRepository;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.time.LocalDateTime.now;

@Component
@RequiredArgsConstructor
public class GroupApplicationValidator {

    private static final Integer PARTICIPATION_LIMIT = 5;
    private final ParticipantRepository participantRepository;
    private final GroupApplicationRepository groupApplicationRepository;

    public void validate(final Group group, final Member member) {
        Integer participationCount = participantRepository.countCurrentParticipationByMember(member, now());
        validateParticipationLimit(participationCount);
        validateDuplicatedApplication(group, member);
    }

    private void validateParticipationLimit(final Integer participationCount) {
        if (participationCount > PARTICIPATION_LIMIT) {
            throw ParticipantException.exceedParticipationLimit();
        }
    }

    private void validateDuplicatedApplication(final Group group, final Member member) {
        if (groupApplicationRepository.existsByGroupAndParticipant(group, member)) {
            throw DuplicatedRequestException.duplicatedGroupApplication();
        }
    }
}
