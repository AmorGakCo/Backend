package com.amorgakco.backend.participationrequest.service;

import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.service.GroupService;
import com.amorgakco.backend.participationrequest.domain.ParticipationRequest;
import com.amorgakco.backend.participationrequest.repository.ParticipationRequestRepository;
import com.amorgakco.backend.participationrequest.service.mapper.ParticipationRequestMapper;
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
public class ParticipationRequestService {

    private final ParticipationRequestRepository participationRequestRepository;
    private final GroupService groupService;
    private final ParticipationRequestMapper participationRequestMapper;
    private final NotificationPublisherFacade notificationPublisherFacade;
    private final MemberService memberService;
    private final ParticipationRequestValidator participationRequestValidator;

    @Transactional
    public void participate(final Long groupId, final Long memberId) {
        final Group group = groupService.getGroupWithHost(groupId);
        final Member requestMember = memberService.getMember(memberId);
        participationRequestValidator.validate(group,requestMember);
        final ParticipationRequest participationRequest =
                participationRequestMapper.toGroupParticipation(group, requestMember);
        participationRequestRepository.save(participationRequest);
        notificationPublisherFacade.send(NotificationCreator.participationRequest(
                requestMember,
                group.getHost(),
                group.getName()
        ));
    }

    @Transactional
    public void approve(final Long groupId, final Long memberId, final Member host) {
        final ParticipationRequest participationRequest = getGroupParticipation(groupId, memberId);
        participationRequest.approve(host);
        notificationPublisherFacade.send(NotificationCreator.participationApprove(
                memberService.getMember(memberId),
                participationRequest.getGroup().getName()
        ));
    }

    private ParticipationRequest getGroupParticipation(final Long groupId, final Long memberId) {
        return participationRequestRepository
                .findByGroupIdAndMemberId(groupId, memberId)
                .orElseThrow(ResourceNotFoundException::participationNotFound);
    }

    @Transactional
    public void reject(final Long groupId, final Long memberId, final Long hostId) {
        final ParticipationRequest participationRequest = getGroupParticipation(groupId, memberId);
        final Member member = memberService.getMember(hostId);
        participationRequest.reject(memberService.getMember(hostId));
        notificationPublisherFacade.send(NotificationCreator.participationReject(
                member,
                participationRequest.getGroup().getName()
        ));
    }
}
