package com.amorgakco.backend.groupparticipation.service;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.service.GroupService;
import com.amorgakco.backend.groupparticipation.domain.GroupParticipation;
import com.amorgakco.backend.groupparticipation.repository.GroupParticiaptionRepository;
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

    private final GroupParticiaptionRepository groupParticiaptionRepository;
    private final GroupService groupService;
    private final GroupParticipationMapper groupParticipationMapper;
    private final NotificationPublisher notificationPublisher;

    @Transactional
    public void participate(final Long groupId, final Member member) {
        final Group group = groupService.getGroup(groupId);
        group.validateParticipation(new Participant(member));
        final GroupParticipation groupParticipation =
                groupParticipationMapper.toGroupParticipation(group, member);
        groupParticiaptionRepository.save(groupParticipation);
        final NotificationRequest notificationRequest =
                NotificationCreator.groupJoiningNotification(member, group.getHost());
        notificationPublisher.sendSmsAndFcmWebPush(notificationRequest);
    }
}
