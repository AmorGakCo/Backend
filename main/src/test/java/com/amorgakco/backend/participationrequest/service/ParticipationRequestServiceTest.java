package com.amorgakco.backend.participationrequest.service;

import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.participationrequest.domain.ParticipationRequest;
import com.amorgakco.backend.participationrequest.domain.ParticipationStatus;
import com.amorgakco.backend.participationrequest.repository.ParticipationRequestRepository;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.repository.MemberRepository;
import com.amorgakco.backend.notification.infrastructure.NotificationPublisherFacade;
import com.amorgakco.backend.notification.infrastructure.consumer.NotificationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@Transactional
class ParticipationRequestServiceTest {

    @Autowired
    private ParticipationRequestService participationRequestService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private ParticipationRequestRepository participationRequestRepository;
    @MockBean
    private NotificationPublisherFacade notificationPublisherFacade;

    @Test
    @DisplayName("사용자는 참여요청을 보낼 수 있다.")
    void requestParticipation() {
        // given
        Member host = TestMemberFactory.createEntity();
        Member requestMember = TestMemberFactory.createEntity();
        memberRepository.save(host);
        memberRepository.save(requestMember);
        Group group = TestGroupFactory.create(host);
        groupRepository.save(group);
        // when
        doNothing().when(notificationPublisherFacade).send(any(NotificationRequest.class));
        participationRequestService.participate(group.getId(), requestMember.getId());
        // then
        ParticipationRequest participationRequest = participationRequestRepository.findByGroupIdAndMemberId(group.getId(), requestMember.getId()).get();
        assertThat(participationRequest.getParticipant().getId()).isEqualTo(requestMember.getId());
        assertThat(participationRequest.getParticipationStatus()).isEqualTo(ParticipationStatus.PENDING);

    }

    @Test
    @DisplayName("모각코 그룹 호스트는 참여 요청을 수락할 수 있다.")
    void approveParticipation() {
        // given
        Member host = TestMemberFactory.createEntity();
        Member member = TestMemberFactory.createEntity();
        Group group = TestGroupFactory.create(host);
        memberRepository.save(host);
        memberRepository.save(member);
        groupRepository.save(group);
        participationRequestRepository.save(new ParticipationRequest(group, member));
        // when
        participationRequestService.approve(group.getId(), member.getId(), host);
        // then
        ParticipationRequest participationRequest = participationRequestRepository.findByGroupIdAndMemberId(group.getId(), member.getId()).get();
        assertThat(participationRequest.getParticipationStatus()).isEqualTo(ParticipationStatus.APPROVED);
    }

    @Test
    @DisplayName("모각코 그룹 호스트는 참여 요청을 거절할 수 있다.")
    void rejectParticipation() {
        // given
        Member host = TestMemberFactory.createEntity();
        Member member = TestMemberFactory.createEntity();
        Group group = TestGroupFactory.create(host);
        memberRepository.save(host);
        memberRepository.save(member);
        groupRepository.save(group);
        participationRequestRepository.save(new ParticipationRequest(group, member));
        // when
        participationRequestService.reject(group.getId(), member.getId(), host.getId());
        // then
        ParticipationRequest participationRequest = participationRequestRepository.findByGroupIdAndMemberId(group.getId(), member.getId()).get();
        assertThat(participationRequest.getParticipationStatus()).isEqualTo(ParticipationStatus.REJECTED);
    }
}