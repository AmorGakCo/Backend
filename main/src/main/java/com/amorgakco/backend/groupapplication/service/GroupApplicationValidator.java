package com.amorgakco.backend.groupapplication.service;

import com.amorgakco.backend.global.exception.DuplicatedRequestException;
import com.amorgakco.backend.global.exception.ParticipantException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.groupapplication.repository.GroupApplicationRepository;
import com.amorgakco.backend.groupparticipant.repository.GroupParticipantRepository;
import com.amorgakco.backend.member.domain.Member;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupApplicationValidator {

    private static final Integer PARTICIPATION_LIMIT = 5;
    private final GroupParticipantRepository groupParticipantRepository;
    private final GroupApplicationRepository groupApplicationRepository;

    public void validate(final Group group, final Member member) {
        Integer participationCount = groupParticipantRepository.countCurrentParticipationByMember(
            member, LocalDateTime.now());
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
