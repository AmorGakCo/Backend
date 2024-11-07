package com.amorgakco.backend.groupapplication.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.amorgakco.backend.fixture.groupparticipation.TestGroupParticipationFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GroupApplicationTest {

    @Test
    @DisplayName("그룹 참여를 허가할 수 있다.")
    void approve() {
        // given
        final Member host = TestMemberFactory.create(1L);
        final Long memberId = 2L;
        final GroupApplication groupApplication =
            TestGroupParticipationFactory.create(host, memberId);
        // when
        groupApplication.approve(host);
        // then
        assertThat(groupApplication.getGroupApplicationStatus())
            .isEqualTo(GroupApplicationStatus.APPROVED);
    }

    @Test
    @DisplayName("그룹 참여를 거절할 수 있다.")
    void reject() {
        // given
        final Member host = TestMemberFactory.create(1L);
        final Long memberId = 2L;
        final GroupApplication groupApplication =
            TestGroupParticipationFactory.create(host, memberId);
        // when
        groupApplication.reject(host);
        // then
        assertThat(groupApplication.getGroupApplicationStatus())
            .isEqualTo(GroupApplicationStatus.REJECTED);
    }
}
