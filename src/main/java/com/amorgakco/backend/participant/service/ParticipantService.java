package com.amorgakco.backend.participant.service;

import com.amorgakco.backend.global.config.redisson.Lock;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.dto.LocationVerificationRequest;
import com.amorgakco.backend.group.service.GroupService;
import com.amorgakco.backend.notification.infrastructure.NotificationPublisherFacade;
import com.amorgakco.backend.notification.service.NotificationCreator;
import com.amorgakco.backend.participant.domain.Participant;
import com.amorgakco.backend.participant.dto.ParticipationHistoryResponse;
import com.amorgakco.backend.participant.dto.TardinessRequest;
import com.amorgakco.backend.participant.repository.ParticipantRepository;
import com.amorgakco.backend.participant.service.mapper.ParticipantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private static final Integer PAGE_SIZE = 10;
    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;
    private final GroupService groupService;
    private final NotificationPublisherFacade notificationPublisherFacade;

    @Transactional
    public void verifyParticipantLocation(
            final LocationVerificationRequest request, final Long memberId) {
        final Participant participant = getParticipant(request.groupId(), memberId);
        participant.verify(request.longitude(), request.latitude());
    }

    public Participant getParticipant(final Long groupId, final Long memberId) {
        return participantRepository
                .findByGroupAndMember(groupId, memberId)
                .orElseThrow(ResourceNotFoundException::participantsNotFound);
    }

    @Transactional(readOnly = true)
    public ParticipationHistoryResponse getParticipationHistory(
            final Long memberId, final Integer page) {
        final PageRequest pageRequest =
                PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
        final Slice<Participant> participantSlice =
                participantRepository.findByMember(memberId, pageRequest);
        return participantMapper.toParticipationHistoryResponse(participantSlice);
    }

    @Transactional
    public void withdraw(final Long groupId, final Long memberId) {
        final Participant participant = getParticipant(groupId, memberId);
        participantRepository.delete(participant);
        final Group group = groupService.getGroup(groupId);
        notificationPublisherFacade.send(NotificationCreator.withdraw(
                participant.getMember(),
                group.getHost(),
                group.getName()
        ));
    }

    @Transactional
    public void tardy(final Long groupId, final Long memberId, final TardinessRequest tardinessRequest) {
        final Participant participant = getParticipant(groupId, memberId);
        final Group group = groupService.getGroup(groupId);
        notificationPublisherFacade.send(NotificationCreator.tardy(
                participant.getMember(),
                group.getHost(),
                group.getName(),
                tardinessRequest.minute()
        ));
    }

    @Lock(key = "#targetMemberId")
    public Integer upTemperature(final Long groupId, final Long requestMemberId, final Long targetMemberId) {
        final Participant requestParticipant = getParticipant(groupId, requestMemberId);
        final Participant targetParticipant = getParticipant(groupId, targetMemberId);
        return targetParticipant.upTemperature(requestParticipant);
    }

    @Lock(key = "#targetMemberId")
    public Integer downTemperature(final Long groupId, final Long requestMemberId, final Long targetMemberId) {
        final Participant requestParticipant = getParticipant(groupId, requestMemberId);
        final Participant targetParticipant = getParticipant(groupId, targetMemberId);
        return targetParticipant.downTemperature(requestParticipant);
    }
}
