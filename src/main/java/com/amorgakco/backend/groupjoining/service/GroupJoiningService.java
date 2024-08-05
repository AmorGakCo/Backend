package com.amorgakco.backend.groupjoining.service;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.domain.Participant;
import com.amorgakco.backend.group.service.GroupService;
import com.amorgakco.backend.groupjoining.domain.GroupJoining;
import com.amorgakco.backend.groupjoining.repository.GroupJoiningRepository;
import com.amorgakco.backend.groupjoining.service.mapper.GroupJoiningMapper;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.notification.dto.NotificationRequest;
import com.amorgakco.backend.notification.service.NotificationCreator;
import com.amorgakco.backend.notification.service.NotificationPublisher;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupJoiningService {

    private final GroupJoiningRepository groupJoiningRepository;
    private final GroupService groupService;
    private final GroupJoiningMapper groupJoiningMapper;
    private final NotificationPublisher notificationPublisher;

    @Transactional
    public void requestJoin(final Long groupId, final Member member) {
        final Group group = groupService.getGroup(groupId);
        group.validateParticipation(new Participant(member));
        final GroupJoining groupJoining = groupJoiningMapper.toGroupJoining(group, member);
        groupJoiningRepository.save(groupJoining);
        final NotificationRequest notificationRequest =
                NotificationCreator.groupJoiningNotification(member, group.getHost());
        notificationPublisher.sendSmsAndFcmWebPush(notificationRequest);
    }
}
