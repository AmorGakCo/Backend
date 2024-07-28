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
    @DisplayName("그룹 참여자가 아닌 멤버는 위치인증을 실패한다.")
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
        assertThatThrownBy(() -> group.verifyLocation(126.97573, 37.5703, 2L))
                .isInstanceOf(IllegalAccessException.class);
    }

    @Test
    @DisplayName("모임 위치에서 50m 안에서는 위치인증을 성공한다.")
    void validateLocation() {
        // given
        final Member host = TestMemberFactory.create(1L);
        final Group group = TestGroupFactory.create(host);
        final Member member = TestMemberFactory.create(2L);
        final Participants participants = TestParticipantsFactory.create(member);
        group.addParticipants(participants);
        // when
        group.verifyLocation(126.9757323, 37.570382, 2L);
        // then
    }
}
