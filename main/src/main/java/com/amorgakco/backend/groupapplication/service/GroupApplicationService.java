package com.amorgakco.backend.groupapplication.service;

import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.service.GroupService;
import com.amorgakco.backend.groupapplication.domain.GroupApplication;
import com.amorgakco.backend.groupapplication.dto.ApplicationRegisterResponse;
import com.amorgakco.backend.groupapplication.dto.ApproveResponse;
import com.amorgakco.backend.groupapplication.dto.RejectResponse;
import com.amorgakco.backend.groupapplication.repository.GroupApplicationRepository;
import com.amorgakco.backend.groupapplication.service.mapper.GroupApplicationMapper;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.service.MemberService;
import com.amorgakco.backend.notification.infrastructure.NotificationPublisherFacade;
import com.amorgakco.backend.notification.service.NotificationCreator;
import com.amorgakco.backend.notification.service.NotificationService;
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
    private final NotificationService notificationService;

    @Transactional
    public ApplicationRegisterResponse apply(final Long groupId, final Long memberId) {
        final Group group = groupService.getGroupWithHost(groupId);
        final Member sender = memberService.getMember(memberId);
        groupApplicationValidator.validate(group, sender);
        final GroupApplication groupApplication =
            groupApplicationMapper.toGroupApplication(group, sender);
        groupApplicationRepository.save(groupApplication);
        notificationPublisherFacade.send(NotificationCreator.participationRequest(
            sender,
            group.getHost(),
            group
        ));
        return new ApplicationRegisterResponse(groupApplication.getId());
    }

    @Transactional
    public ApproveResponse approve(final Long groupId, final Long memberId, final Member member,final Long notificationId) {
        final GroupApplication groupApplication = getGroupParticipation(groupId, memberId);
        groupApplication.approve(member);
        Group group = groupApplication.getGroup();
        notificationPublisherFacade.send(NotificationCreator.participationApprove(
            group.getHost(),
            groupApplication.getApplicant(),
            group
        ));
        notificationService.deleteNotification(notificationId);
        return new ApproveResponse(memberId);
    }

    private GroupApplication getGroupParticipation(final Long groupId, final Long memberId) {
        return groupApplicationRepository
            .findByGroupIdAndMemberId(groupId, memberId)
            .orElseThrow(ResourceNotFoundException::participationNotFound);
    }

    @Transactional
    public RejectResponse reject(final Long groupId, final Long memberId, final Long hostId,
        final Long notificationId) {
        final GroupApplication groupApplication = getGroupParticipation(groupId, memberId);
        groupApplication.reject(memberService.getMember(hostId));
        final Group group = groupApplication.getGroup();
        notificationPublisherFacade.send(NotificationCreator.participationReject(
            group.getHost(),
            groupApplication.getApplicant(),
            group
        ));
        notificationService.deleteNotification(notificationId);
        return new RejectResponse(memberId);
    }
}
