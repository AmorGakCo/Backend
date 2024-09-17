package com.amorgakco.backend.participant.service;

import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.dto.LocationVerificationRequest;
import com.amorgakco.backend.group.service.GroupService;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.notification.infrastructure.NotificationPublisher;
import com.amorgakco.backend.notification.infrastructure.consumer.NotificationRequest;
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
@Transactional(readOnly = true)
public class ParticipantService {

    private static final Integer PAGE_SIZE = 10;
    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;
    private final GroupService groupService;
    private final NotificationPublisher notificationPublisher;

    @Transactional
    public void verifyParticipantLocation(
            final LocationVerificationRequest request, final Long memberId) {
        final Participant participant = getParticipant(request.groupId(), memberId);
        participant.verify(request.longitude(), request.latitude());
        if (isEveryParticipantsVerified(participant)) {
            notificationPublisher.sendFcmWebPush();
        }
    }

    private boolean isEveryParticipantsVerified(final Participant participant) {
        final Group group = participant.getGroup();
        return group.isEveryParticipantsVerified();
    }

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
        final Member host = groupService.getGroup(groupId).getHost();
        final NotificationRequest notificationRequest =
                NotificationCreator.withdrawNotification(participant.getMember(), host);
        notificationPublisher.sendFcmWebPush(notificationRequest);
    }

    @Transactional
    public void tardy(final Long groupId, final Long memberId, final TardinessRequest request) {
        final Participant participant = getParticipant(groupId, memberId);
        final Member host = groupService.getGroup(groupId).getHost();
        final NotificationRequest notificationRequest =
                NotificationCreator.tardinessNotification(participant.getMember(), host, request.minute());
        notificationPublisher.sendFcmWebPush(notificationRequest);
    }

    private Participant getParticipant(final Long groupId, final Long memberId) {
        return participantRepository
                .findByGroupAndMember(groupId, memberId)
                .orElseThrow(ResourceNotFoundException::participantsNotFound);
    }

    @Transactional
    public Integer upTemperature(final Long groupId, final Long requestMemberId, final Long targetMemberId) {
        final Participant requestParticipant = getParticipant(groupId, requestMemberId);
        final Participant targetParticipant = getParticipant(groupId, targetMemberId);
        return targetParticipant.upTemperature(requestParticipant);
    }

    @Transactional
    public Integer downTemperature(final Long groupId, final Long requestMemberId, final Long targetMemberId) {
        final Participant requestParticipant = getParticipant(groupId, requestMemberId);
        final Participant targetParticipant = getParticipant(groupId, targetMemberId);
        return targetParticipant.downTemperature(requestParticipant);
    }
}
