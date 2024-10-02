package com.amorgakco.backend.groupparticipation.service;

import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.groupparticipation.domain.GroupParticipation;
import com.amorgakco.backend.groupparticipation.domain.ParticipationStatus;
import com.amorgakco.backend.groupparticipation.repository.GroupParticipationRepository;
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
class GroupParticipationServiceTest {

    @Autowired
    private GroupParticipationService groupParticipationService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupParticipationRepository groupParticipationRepository;
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
        groupParticipationService.participate(group.getId(), requestMember.getId());
        // then
        GroupParticipation groupParticipation = groupParticipationRepository.findByGroupIdAndMemberId(group.getId(), requestMember.getId()).get();
        assertThat(groupParticipation.getParticipant().getId()).isEqualTo(requestMember.getId());
        assertThat(groupParticipation.getParticipationStatus()).isEqualTo(ParticipationStatus.PENDING);

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
        groupParticipationRepository.save(new GroupParticipation(group, member));
        // when
        groupParticipationService.approve(group.getId(), member.getId(), host);
        // then
        GroupParticipation groupParticipation = groupParticipationRepository.findByGroupIdAndMemberId(group.getId(), member.getId()).get();
        assertThat(groupParticipation.getParticipationStatus()).isEqualTo(ParticipationStatus.APPROVED);
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
        groupParticipationRepository.save(new GroupParticipation(group, member));
        // when
        groupParticipationService.reject(group.getId(), member.getId(), host.getId());
        // then
        GroupParticipation groupParticipation = groupParticipationRepository.findByGroupIdAndMemberId(group.getId(), member.getId()).get();
        assertThat(groupParticipation.getParticipationStatus()).isEqualTo(ParticipationStatus.REJECTED);
    }
}