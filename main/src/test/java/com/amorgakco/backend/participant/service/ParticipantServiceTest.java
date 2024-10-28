package com.amorgakco.backend.participant.service;

import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.repository.MemberRepository;
import com.amorgakco.backend.notification.infrastructure.NotificationPublisherFacade;
import com.amorgakco.backend.notification.infrastructure.consumer.NotificationRequest;
import com.amorgakco.backend.participant.domain.Participant;
import com.amorgakco.backend.participant.dto.ParticipationHistoryResponse;
import com.amorgakco.backend.participant.repository.ParticipantRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@Transactional
class ParticipantServiceTest {
    @Autowired
    private ParticipantService participantService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GroupRepository groupRepository;
    @MockBean
    private NotificationPublisherFacade notificationPublisherFacade;

    @Test
    @DisplayName("종료된 모각코와 진행중인 모각코의 내역을 확인할 수 있다.")
    void groupHistory() {
        // given
        Member member = TestMemberFactory.createEntity();
        memberRepository.save(member);
        final int activeGroupSize = 3;
        final int inactiveGroupSize = 10;
        createAndSaveTestGroups(member, activeGroupSize, inactiveGroupSize);
        // when
        ParticipationHistoryResponse history = participantService.getParticipationHistory(member.getId(), 0);
        // then
        assertThat(history.activatedGroup().size()).isEqualTo(activeGroupSize);
        assertThat(history.elementSize()).isEqualTo(10);
        assertThat(history.InactivatedGroup().size()).isEqualTo(inactiveGroupSize);
        assertThat(history.page()).isEqualTo(0);
        assertThat(history.hasNext()).isFalse();
    }

    private void createAndSaveTestGroups(final Member member,final int activeGroupSize, final int inactiveGroupSize) {
        for (int i = 0; i < activeGroupSize; i++) {
            Group group = TestGroupFactory.create(member);
            groupRepository.save(group);
        }
        for (int i = 0; i < inactiveGroupSize; i++) {
            Group group = TestGroupFactory.createInactiveGroup(member);
            groupRepository.save(group);
        }
    }

    @Test
    @DisplayName("참여자는 모각코를 탈퇴할 수 있다.")
    void withdrawGroup() {
        // given
        Member host = TestMemberFactory.createEntity();
        Member member = TestMemberFactory.createEntity();
        memberRepository.save(member);
        memberRepository.save(host);
        Group group = TestGroupFactory.create(host);
        group.addParticipants(new Participant(member));
        groupRepository.save(group);
        // when
        doNothing().when(notificationPublisherFacade).send(any(NotificationRequest.class));
        participantService.withdraw(group.getId(), member.getId());
        // then
        assertThat(!group.getParticipants().contains(member)).isTrue();
    }
}
