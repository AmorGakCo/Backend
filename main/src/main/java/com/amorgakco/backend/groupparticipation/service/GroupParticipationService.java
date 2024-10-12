package com.amorgakco.backend.groupparticipation.service;

import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.service.GroupService;
import com.amorgakco.backend.groupparticipation.domain.GroupParticipation;
import com.amorgakco.backend.groupparticipation.repository.GroupParticipationRepository;
import com.amorgakco.backend.groupparticipation.service.mapper.GroupParticipationMapper;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.service.MemberService;
import com.amorgakco.backend.notification.infrastructure.NotificationPublisherFacade;
import com.amorgakco.backend.notification.service.NotificationCreator;
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
    private final NotificationPublisherFacade notificationPublisherFacade;
    private final MemberService memberService;

    @Transactional
    public void participate(final Long groupId, final Long memberId) {
        final Group group = groupService.getGroupWithHost(groupId);
        final Member requestMember = memberService.getMember(memberId);
        group.validateParticipation(new Participant(requestMember));
        final GroupParticipation groupParticipation =
                groupParticipationMapper.toGroupParticipation(group, requestMember);
        groupParticipationRepository.save(groupParticipation);
        notificationPublisherFacade.send(NotificationCreator.participationRequest(
                requestMember,
                group.getHost(),
                group.getName()
        ));
    }

    @Transactional
    public void approve(final Long groupId, final Long memberId, final Member host) {
        final GroupParticipation groupParticipation = getGroupParticipation(groupId, memberId);
        groupParticipation.approve(host);
        notificationPublisherFacade.send(NotificationCreator.participationApprove(
                memberService.getMember(memberId),
                groupParticipation.getGroup().getName()
        ));
    }

    private GroupParticipation getGroupParticipation(final Long groupId, final Long memberId) {
        return groupParticipationRepository
                .findByGroupIdAndMemberId(groupId, memberId)
                .orElseThrow(ResourceNotFoundException::participationNotFound);
    }

    @Transactional
    public void reject(final Long groupId, final Long memberId, final Long hostId) {
        final GroupParticipation groupParticipation = getGroupParticipation(groupId, memberId);
        final Member member = memberService.getMember(hostId);
        groupParticipation.reject(memberService.getMember(hostId));
        notificationPublisherFacade.send(NotificationCreator.participationReject(
                member,
                groupParticipation.getGroup().getName()
        ));
    }
}
