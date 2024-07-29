package com.amorgakco.backend.group.domain;

import static org.assertj.core.api.Assertions.*;

import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.fixture.group.TestParticipantsFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.global.exception.IllegalAccessException;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.member.domain.Member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GroupTest {

    @Test
    @DisplayName("그룹 참여인원은 참여자와 호스트 더해 반환받을 수 있다")
    void getCurrentGroupSize() {
        // given
        final Member host = TestMemberFactory.create(1L);
        final Group group = TestGroupFactory.create(host);
        final Member member = TestMemberFactory.create(2L);
        final Participants participants = TestParticipantsFactory.create(member);
        group.addParticipants(participants);
        // when
        final int currentGroupSize = group.getCurrentGroupSize();
        // then
        assertThat(currentGroupSize).isEqualTo(2);
    }

    @Test
    @DisplayName("모각코 참여자가 아닌 멤버는 위치인증을 실패한다.")
    void validateParticipants() {
        // given
        final Member host = TestMemberFactory.create(1L);
        final Group group = TestGroupFactory.create(host);
        // then
        assertThatThrownBy(() -> group.verifyLocation(126.9748397, 37.5703901, 1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("모임 위치에서 50m 밖에서는 위치인증을 실패한다.")
    void validateLocationException() {
        // given
        final Member host = TestMemberFactory.create(1L);
        final Group group = TestGroupFactory.create(host);
        final Member member = TestMemberFactory.create(2L);
        final Participants participants = TestParticipantsFactory.create(member);
        group.addParticipants(participants);
        // when
        assertThatThrownBy(() -> group.verifyLocation(126.9754143, 37.57071, 2L))
                .isInstanceOf(IllegalAccessException.class);
    }

    @Test
    @DisplayName("위치인증에 성공하면 참여자의 상태가 VERIFIED로 변경된다.")
    void validateLocation() {
        // given
        final Member host = TestMemberFactory.create(1L);
        final Group group = TestGroupFactory.create(host);
        final Member member = TestMemberFactory.create(2L);
        final Participants participants = TestParticipantsFactory.create(member);
        group.addParticipants(participants);
        // when
        group.verifyLocation(126.9745357, 37.570387, 2L);
        // then
        assertThat(participants.getLocationVerificationStatus())
                .isEqualTo(LocationVerificationStatus.VERIFIED);
    }

    @Test
    @DisplayName("이미 인증한 회원은 다시 인증할 수 없다.")
    void duplicatedVerification() {
        // given
        final Member host = TestMemberFactory.create(1L);
        final Group group = TestGroupFactory.create(host);
        final Member member = TestMemberFactory.create(2L);
        final Participants participants = TestParticipantsFactory.create(member);
        group.addParticipants(participants);
        group.verifyLocation(126.9745357, 37.570387, 2L);
        // when&then
        assertThatThrownBy(() -> group.verifyLocation(126.9745357, 37.570387, 2L))
                .isInstanceOf(IllegalAccessException.class);
    }
}