package com.amorgakco.backend.groupapplication.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doNothing;

import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.groupapplication.domain.GroupApplication;
import com.amorgakco.backend.groupapplication.domain.GroupApplicationStatus;
import com.amorgakco.backend.groupapplication.repository.GroupApplicationRepository;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.repository.MemberRepository;
import com.amorgakco.backend.notification.infrastructure.NotificationPublisherFacade;
import com.amorgakco.backend.notification.dto.NotificationRequest;
import com.amorgakco.backend.notification.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class GroupApplicationServiceTest {

    @Autowired
    private GroupApplicationService groupApplicationService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupApplicationRepository groupApplicationRepository;
    @MockBean
    private NotificationPublisherFacade notificationPublisherFacade;
    @MockBean
    private NotificationService notificationService;

    @Test
    @DisplayName("사용자는 참여요청을 보낼 수 있다.")
    void requestParticipation() {
        // given
        Member host = TestMemberFactory.createEntity();
        Member requestMember = TestMemberFactory.createEntity();
        memberRepository.save(host);
        memberRepository.save(requestMember);
        Group group = TestGroupFactory.createActiveGroup(host);
        groupRepository.save(group);
        // when
        doNothing().when(notificationPublisherFacade).send(any(NotificationRequest.class));
        groupApplicationService.apply(group.getId(), requestMember.getId());
        // then
        GroupApplication groupApplication = groupApplicationRepository.findByGroupIdAndMemberId(
            group.getId(), requestMember.getId()).get();
        assertThat(groupApplication.getApplicant().getId()).isEqualTo(requestMember.getId());
        assertThat(groupApplication.getGroupApplicationStatus()).isEqualTo(
            GroupApplicationStatus.PENDING);

    }

    @Test
    @DisplayName("모각코 그룹 호스트는 참여 요청을 수락할 수 있다.")
    void approveParticipation() {
        // given
        Member host = TestMemberFactory.createEntity();
        Member member = TestMemberFactory.createEntity();
        Group group = TestGroupFactory.createActiveGroup(host);
        Long notificationId = 1L;
        memberRepository.save(host);
        memberRepository.save(member);
        groupRepository.save(group);
        groupApplicationRepository.save(new GroupApplication(group, member));
        given(notificationService.deleteNotification(notificationId)).willReturn(null);
        // when
        groupApplicationService.approve(group.getId(), member.getId(), host,notificationId);
        // then
        GroupApplication groupApplication = groupApplicationRepository.findByGroupIdAndMemberId(
            group.getId(), member.getId()).get();
        assertThat(groupApplication.getGroupApplicationStatus()).isEqualTo(
            GroupApplicationStatus.APPROVED);
    }

    @Test
    @DisplayName("모각코 그룹 호스트는 참여 요청을 거절할 수 있다.")
    void rejectParticipation() {
        // given
        Member host = TestMemberFactory.createEntity();
        Member member = TestMemberFactory.createEntity();
        Group group = TestGroupFactory.createActiveGroup(host);
        Long notificationId = 1L;
        memberRepository.save(host);
        memberRepository.save(member);
        groupRepository.save(group);
        groupApplicationRepository.save(new GroupApplication(group, member));
        given(notificationService.deleteNotification(notificationId)).willReturn(null);
        // when
        groupApplicationService.reject(group.getId(), member.getId(), host.getId(),notificationId);
        // then
        GroupApplication groupApplication = groupApplicationRepository.findByGroupIdAndMemberId(
            group.getId(), member.getId()).get();
        assertThat(groupApplication.getGroupApplicationStatus()).isEqualTo(
            GroupApplicationStatus.REJECTED);
    }
}