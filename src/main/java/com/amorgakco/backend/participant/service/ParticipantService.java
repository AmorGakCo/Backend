package com.amorgakco.backend.participant.service;

import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.dto.LocationVerificationRequest;
import com.amorgakco.backend.participant.domain.Participant;
import com.amorgakco.backend.participant.dto.ParticipationHistoryResponse;
import com.amorgakco.backend.participant.repository.ParticipantRepository;
import com.amorgakco.backend.participant.service.mapper.ParticipantMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private static final Integer PAGE_SIZE = 10;
    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;

    public void verifyParticipantLocation(
            final LocationVerificationRequest request, final Long memberId) {
        final Participant participant =
                participantRepository
                        .findByGroupAndMember(request.groupId(), memberId)
                        .orElseThrow(ResourceNotFoundException::participantsNotFound);
        participant.verify(request.longitude(), request.latitude());
    }

    public ParticipationHistoryResponse getParticipationHistory(
            final Long memberId, final Integer page) {
        final PageRequest pageRequest =
                PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
        final Slice<Participant> participantSlice =
                participantRepository.findByMember(memberId, pageRequest);
        return participantMapper.toParticipationHistoryResponse(participantSlice);
    }
}
