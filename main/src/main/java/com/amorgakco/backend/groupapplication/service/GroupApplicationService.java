package com.amorgakco.backend.groupapplication.service;

import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.service.GroupService;
import com.amorgakco.backend.groupapplication.domain.GroupApplication;
import com.amorgakco.backend.groupapplication.dto.ApplicationResponse;
import com.amorgakco.backend.groupapplication.repository.GroupApplicationRepository;
import com.amorgakco.backend.groupapplication.service.mapper.GroupApplicationMapper;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.service.MemberService;
import com.amorgakco.backend.notification.infrastructure.NotificationPublisherFacade;
import com.amorgakco.backend.notification.service.NotificationCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupApplicationService {

    private final GroupApplicationRepository groupApplicationRepository;
    private final GroupService groupService;
    private final GroupApplicationMapper groupApplicationMapper;
    private final NotificationPublisherFacade notificationPublisherFacade;
    private final MemberService memberService;
    private final GroupApplicationValidator groupApplicationValidator;

    @Transactional
    public ApplicationResponse apply(final Long groupId, final Long memberId) {
        final Group group = groupService.getGroupWithHost(groupId);
        final Member requestMember = memberService.getMember(memberId);
        groupApplicationValidator.validate(group, requestMember);
        final GroupApplication groupApplication =
            groupApplicationMapper.toGroupApplication(group, requestMember);
        groupApplicationRepository.save(groupApplication);
        notificationPublisherFacade.send(NotificationCreator.participationRequest(
            requestMember,
            group.getHost(),
            group.getName()
        ));
        return new ApplicationResponse(groupApplication.getId());
    }

    @Transactional
    public void approve(final Long groupId, final Long memberId, final Member member) {
        final GroupApplication groupApplication = getGroupParticipation(groupId, memberId);
        groupApplication.approve(member);
        notificationPublisherFacade.send(NotificationCreator.participationApprove(
            memberService.getMember(memberId),
            groupApplication.getGroup().getName()
        ));
    }

    private GroupApplication getGroupParticipation(final Long groupId, final Long memberId) {
        return groupApplicationRepository
            .findByGroupIdAndMemberId(groupId, memberId)
            .orElseThrow(ResourceNotFoundException::participationNotFound);
    }

    @Transactional
    public void reject(final Long groupId, final Long memberId, final Long hostId) {
        final GroupApplication groupApplication = getGroupParticipation(groupId, memberId);
        final Member member = memberService.getMember(hostId);
        groupApplication.reject(memberService.getMember(hostId));
        notificationPublisherFacade.send(NotificationCreator.participationReject(
            member,
            groupApplication.getGroup().getName()
        ));
    }
}
