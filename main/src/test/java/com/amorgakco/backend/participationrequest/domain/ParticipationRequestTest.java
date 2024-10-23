package com.amorgakco.backend.participationrequest.domain;

import com.amorgakco.backend.fixture.groupparticipation.TestGroupParticipationFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParticipationRequestTest {

    @Test
    @DisplayName("그룹 참여를 허가할 수 있다.")
    void approve() {
        // given
        final Member host = TestMemberFactory.create(1L);
        final Long memberId = 2L;
        final ParticipationRequest participationRequest =
                TestGroupParticipationFactory.create(host, memberId);
        // when
        participationRequest.approve(host);
        // then
        assertThat(participationRequest.getParticipationStatus())
                .isEqualTo(ParticipationStatus.APPROVED);
    }

    @Test
    @DisplayName("그룹 참여를 거절할 수 있다.")
    void reject() {
        // given
        final Member host = TestMemberFactory.create(1L);
        final Long memberId = 2L;
        final ParticipationRequest participationRequest =
                TestGroupParticipationFactory.create(host, memberId);
        // when
        participationRequest.reject(host);
        // then
        assertThat(participationRequest.getParticipationStatus())
                .isEqualTo(ParticipationStatus.REJECTED);
    }
}
