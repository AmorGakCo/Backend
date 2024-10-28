package com.amorgakco.backend.group.domain;

import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.fixture.group.TestParticipantsFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.global.exception.ErrorCode;
import com.amorgakco.backend.global.exception.GroupAuthorityException;
import com.amorgakco.backend.global.exception.LocationVerificationException;
import com.amorgakco.backend.global.exception.ParticipantException;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.participant.domain.Participant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GroupTest {

    @Test
    @DisplayName("그룹 참여인원은 참여자와 호스트 더해 반환받을 수 있다")
    void getCurrentGroupSize() {
        // given
        final Member host = TestMemberFactory.create(1L);
        final Group group = TestGroupFactory.create(host);
        final Member member = TestMemberFactory.create(2L);
        final int expectGroupSize = 2;
        final Participant participant = TestParticipantsFactory.create(member);
        group.addParticipants(participant);
        // when
        final int currentGroupSize = group.getCurrentGroupSize();
        // then
        assertThat(currentGroupSize).isEqualTo(expectGroupSize);
    }

    @Test
    @DisplayName("참여자의 중복참여는 불가능하다")
    void duplicateParticipation() {
        // given
        final Member host = TestMemberFactory.create(1L);
        final Group group = TestGroupFactory.create(host);
        final Participant participant = new Participant(TestMemberFactory.create(2L));
        group.addParticipants(participant);
        final Participant newParticipant = new Participant(TestMemberFactory.create(2L));
        // when
        assertThatThrownBy(() -> group.addParticipants(newParticipant))
                .isInstanceOf(ParticipantException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.PARTICIPANT_DUPLICATED);
    }

    @Test
    @DisplayName("모임 위치에서 50m 밖에서는 위치인증을 실패한다.")
    void validateLocationException() {
        // given
        final Member host = TestMemberFactory.create(1L);
        final Group group = TestGroupFactory.create(host);
        final Member member = TestMemberFactory.create(2L);
        final Participant participant = TestParticipantsFactory.create(member);
        group.addParticipants(participant);
        // when
        assertThatThrownBy(() -> group.verifyLocation(126.9754143, 37.57071))
                .isInstanceOf(LocationVerificationException.class);
    }
}
