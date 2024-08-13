package com.amorgakco.backend.groupparticipation.service;

import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.service.GroupService;
import com.amorgakco.backend.groupparticipation.domain.GroupParticipation;
import com.amorgakco.backend.groupparticipation.repository.GroupParticipationRepository;
import com.amorgakco.backend.groupparticipation.service.mapper.GroupParticipationMapper;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.notification.infrastructure.NotificationPublisher;
import com.amorgakco.backend.notification.infrastructure.consumer.NotificationCreator;
import com.amorgakco.backend.notification.infrastructure.consumer.NotificationRequest;
import com.amorgakco.backend.participant.domain.Participant;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupParticipationService {

    private final GroupParticipationRepository groupParticipationRepository;
    private final GroupService groupService;
    private final GroupParticipationMapper groupParticipationMapper;
    private final NotificationPublisher notificationPublisher;

    @Transactional
    public void participate(final Long groupId, final Member member) {
        final Group group = groupService.getGroup(groupId);
        group.validateParticipation(new Participant(member));
        final GroupParticipation groupParticipation =
                groupParticipationMapper.toGroupParticipation(group, member);
        groupParticipationRepository.save(groupParticipation);
        final NotificationRequest notificationRequest =
                NotificationCreator.participationNotification(member, group.getHost());
        notificationPublisher.sendSmsAndFcmWebPush(notificationRequest);
    }

    @Transactional
    public void approve(final Long groupId, final Long memberId, final Member host) {
        final GroupParticipation groupParticipation =
                groupParticipationRepository
                        .findByGroupIdAndMemberId(groupId, memberId)
                        .orElseThrow(ResourceNotFoundException::participationNotFound);
        groupParticipation.approve(host);
        final NotificationRequest notificationRequest =
                NotificationCreator.participationApproveNotification(
                        host, groupParticipation.getParticipant());
        notificationPublisher.sendSmsAndFcmWebPush(notificationRequest);
    }

    @Transactional
    public void reject(final Long groupId, final Long memberId, final Member host) {
        final GroupParticipation groupParticipation =
                groupParticipationRepository
                        .findByGroupIdAndMemberId(groupId, memberId)
                        .orElseThrow(ResourceNotFoundException::participationNotFound);
        groupParticipation.reject(host);
        final NotificationRequest notificationRequest =
                NotificationCreator.participationRejectNotification(
                        host, groupParticipation.getParticipant());
        notificationPublisher.sendFcmWebPush(notificationRequest);
    }
}
