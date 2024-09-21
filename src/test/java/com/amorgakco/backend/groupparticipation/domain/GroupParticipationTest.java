package com.amorgakco.backend.groupparticipation.domain;

import com.amorgakco.backend.fixture.groupparticipation.TestGroupParticipationFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GroupParticipationTest {

    @Test
    @DisplayName("그룹 참여를 허가할 수 있다.")
    void approve() {
        // given
        final Member host = TestMemberFactory.create(1L);
        final Long memberId = 2L;
        final GroupParticipation groupParticipation =
                TestGroupParticipationFactory.create(host, memberId);
        // when
        groupParticipation.approve(host);
        // then
        assertThat(groupParticipation.getParticipationStatus())
                .isEqualTo(ParticipationStatus.APPROVED);
    }

    @Test
    @DisplayName("그룹 참여를 거절할 수 있다.")
    void reject() {
        // given
        final Member host = TestMemberFactory.create(1L);
        final Long memberId = 2L;
        final GroupParticipation groupParticipation =
                TestGroupParticipationFactory.create(host, memberId);
        // when
        groupParticipation.reject(host);
        // then
        assertThat(groupParticipation.getParticipationStatus())
                .isEqualTo(ParticipationStatus.REJECTED);
    }
}
