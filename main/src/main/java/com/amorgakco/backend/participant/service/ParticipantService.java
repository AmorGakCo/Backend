package com.amorgakco.backend.participant.service;

import com.amorgakco.backend.global.annotation.OptimisticLockRetryable;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.global.exception.RetryFailedException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.dto.LocationVerificationRequest;
import com.amorgakco.backend.group.service.GroupService;
import com.amorgakco.backend.notification.infrastructure.NotificationPublisherFacade;
import com.amorgakco.backend.notification.service.NotificationCreator;
import com.amorgakco.backend.participant.domain.Participant;
import com.amorgakco.backend.participant.dto.ParticipationHistoryPagingResponse;
import com.amorgakco.backend.participant.dto.TardinessRequest;
import com.amorgakco.backend.participant.dto.TemperatureResponse;
import com.amorgakco.backend.participant.repository.ParticipantRepository;
import com.amorgakco.backend.participant.service.mapper.ParticipantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private static final Integer ACTIVE_GROUP_PAGE_SIZE = 5;
    private static final Integer INACTIVE_GROUP_PAGE_SIZE = 10;
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
    public ParticipationHistoryPagingResponse getCurrentParticipationHistories(
            final Long memberId, final Integer page) {
        final PageRequest pageRequest =
                PageRequest.of(page, ACTIVE_GROUP_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
        final Slice<Participant> participantSlice =
                participantRepository.findCurrentParticipationByMember(memberId, now(), pageRequest);
        return participantMapper.toParticipationHistoryPagingResponse(participantSlice);
    }

    @Transactional(readOnly = true)
    public ParticipationHistoryPagingResponse getPastParticipationHistories(
            final Long memberId, final Integer page) {
        final PageRequest pageRequest =
                PageRequest.of(page, INACTIVE_GROUP_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
        final Slice<Participant> participantSlice =
                participantRepository.findPastParticipationByMember(memberId, now(), pageRequest);
        return participantMapper.toParticipationHistoryPagingResponse(participantSlice);
    }

    @Transactional
    public void withdraw(final Long groupId, final Long memberId) {
        final Participant participant = getParticipant(groupId, memberId);
        participantRepository.delete(participant);
        final Group group = groupService.getGroupWithHost(groupId);
        notificationPublisherFacade.send(NotificationCreator.withdraw(
                participant.getMember(),
                group.getHost(),
                group.getName()
        ));
    }

    @Transactional
    public void tardy(final Long groupId, final Long memberId, final TardinessRequest tardinessRequest) {
        final Participant participant = getParticipant(groupId, memberId);
        final Group group = groupService.getGroupWithHost(groupId);
        notificationPublisherFacade.send(NotificationCreator.tardy(
                participant.getMember(),
                group.getHost(),
                group.getName(),
                tardinessRequest.minute()
        ));
    }


    @Transactional
    @OptimisticLockRetryable(recover = "temperatureRecover")
    public TemperatureResponse increaseTemperature(final Long groupId, final Long requestMemberId, final Long targetMemberId) {
        final Participant requestParticipant = getParticipant(groupId, requestMemberId);
        final Participant targetParticipant = getParticipant(groupId, targetMemberId);
        final Integer temperature = targetParticipant.increaseTemperature(requestParticipant);
        return new TemperatureResponse(temperature);
    }

    @Transactional
    @OptimisticLockRetryable(recover = "temperatureRecover")
    public TemperatureResponse decreaseTemperature(final Long groupId, final Long requestMemberId, final Long targetMemberId) {
        final Participant requestParticipant = getParticipant(groupId, requestMemberId);
        final Participant targetParticipant = getParticipant(groupId, targetMemberId);
        final Integer temperature = targetParticipant.decreaseTemperature(requestParticipant);
        return new TemperatureResponse(temperature);
    }

    @Recover
    public TemperatureResponse temperatureRecover(final Long groupId, final Long requestMemberId, final Long targetMemberId) {
        throw RetryFailedException.retryFailed();
    }
}
