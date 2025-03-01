package com.amorgakco.backend.groupparticipant.service;

import com.amorgakco.backend.global.annotation.OptimisticLockRetryable;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.global.exception.RetryFailedException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.dto.LocationVerificationRequest;
import com.amorgakco.backend.group.service.GroupService;
import com.amorgakco.backend.groupparticipant.domain.GroupParticipant;
import com.amorgakco.backend.groupparticipant.dto.GroupParticipationHistoryResponse;
import com.amorgakco.backend.groupparticipant.dto.LocationVerificationResponse;
import com.amorgakco.backend.groupparticipant.dto.TardinessRequest;
import com.amorgakco.backend.groupparticipant.dto.TemperatureResponse;
import com.amorgakco.backend.groupparticipant.repository.GroupParticipantRepository;
import com.amorgakco.backend.groupparticipant.service.mapper.GroupParticipantMapper;
import com.amorgakco.backend.notification.infrastructure.NotificationPublisherFacade;
import com.amorgakco.backend.notification.service.NotificationCreator;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupParticipantService {

    private static final Integer ACTIVE_GROUP_PAGE_SIZE = 5;
    private static final Integer INACTIVE_GROUP_PAGE_SIZE = 10;
    private final GroupParticipantRepository groupParticipantRepository;
    private final GroupParticipantMapper groupParticipantMapper;
    private final GroupService groupService;
    private final NotificationPublisherFacade notificationPublisherFacade;

    @Transactional
    public LocationVerificationResponse verifyParticipantLocation(
        final LocationVerificationRequest request, final Long memberId) {
        final GroupParticipant groupParticipant = getGroupParticipant(request.groupId(), memberId);
        groupParticipant.verify(request.longitude(), request.latitude());
        return new LocationVerificationResponse(memberId);
    }

    public GroupParticipant getGroupParticipant(final Long groupId, final Long memberId) {
        return groupParticipantRepository
            .findByGroupAndMember(groupId, memberId)
            .orElseThrow(ResourceNotFoundException::groupParticipantsNotFound);
    }

    @Transactional(readOnly = true)
    public GroupParticipationHistoryResponse getCurrentGroupParticipationHistories(
        final Long memberId, final Integer page) {
        final PageRequest pageRequest =
            PageRequest.of(page, ACTIVE_GROUP_PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
        final Slice<GroupParticipant> participantSlice =
            groupParticipantRepository.findCurrentParticipationByMember(memberId,
                LocalDateTime.now(), pageRequest);
        return groupParticipantMapper.toParticipationHistoryPagingResponse(participantSlice);
    }

    @Transactional(readOnly = true)
    public GroupParticipationHistoryResponse getPastGroupParticipationHistories(
        final Long memberId, final Integer page) {
        final PageRequest pageRequest =
            PageRequest.of(page, INACTIVE_GROUP_PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, "createdAt"));
        final Slice<GroupParticipant> participantSlice =
            groupParticipantRepository.findPastParticipationByMember(memberId, LocalDateTime.now(),
                pageRequest);
        return groupParticipantMapper.toParticipationHistoryPagingResponse(participantSlice);
    }

    @Transactional
    public void withdraw(final Long groupId, final Long memberId) {
        final GroupParticipant groupParticipant = getGroupParticipant(groupId, memberId);
        groupParticipantRepository.delete(groupParticipant);
        notificationPublisherFacade.send(NotificationCreator.withdraw(groupParticipant));
    }

    @Transactional
    public void tardy(final Long groupId, final Long memberId,
        final TardinessRequest tardinessRequest) {
        final GroupParticipant groupParticipant = getGroupParticipant(groupId, memberId);
        Group group = groupService.getGroup(groupId);
        notificationPublisherFacade.send(NotificationCreator.tardy(
            groupParticipant,
            tardinessRequest.minute(),
            group
        ));
    }

    @Transactional
    @OptimisticLockRetryable(recover = "temperatureRecover")
    public TemperatureResponse increaseTemperature(final Long groupId, final Long requestMemberId,
        final Long targetMemberId) {
        final GroupParticipant requestGroupParticipant = getGroupParticipant(groupId,
            requestMemberId);
        final GroupParticipant targetGroupParticipant = getGroupParticipant(groupId,
            targetMemberId);
        final Integer temperature = targetGroupParticipant.increaseTemperature(
            requestGroupParticipant);
        return new TemperatureResponse(temperature);
    }

    @Transactional
    @OptimisticLockRetryable(recover = "temperatureRecover")
    public TemperatureResponse decreaseTemperature(final Long groupId, final Long requestMemberId,
        final Long targetMemberId) {
        final GroupParticipant requestGroupParticipant = getGroupParticipant(groupId,
            requestMemberId);
        final GroupParticipant targetGroupParticipant = getGroupParticipant(groupId,
            targetMemberId);
        final Integer temperature = targetGroupParticipant.decreaseTemperature(
            requestGroupParticipant);
        return new TemperatureResponse(temperature);
    }

    @Recover
    public TemperatureResponse temperatureRecover(final Long groupId, final Long requestMemberId,
        final Long targetMemberId) {
        throw RetryFailedException.retryFailed();
    }
}
