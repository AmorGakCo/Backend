package com.amorgakco.backend.groupparticipant.domain;

import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.fixture.group.TestParticipantsFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.global.exception.ErrorCode;
import com.amorgakco.backend.global.exception.LocationVerificationException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GroupParticipantTest {

    @Test
    @DisplayName("인증에 성공하면 인증 상태가 VERIFIED로 변경된다.")
    void verifiedLocation() {
        // given
        final Member host = TestMemberFactory.create(1L);
        final Group group = TestGroupFactory.createActiveGroup(host);
        final Member member = TestMemberFactory.create(2L);
        final GroupParticipant groupParticipant = TestParticipantsFactory.create(member);
        group.addParticipant(groupParticipant);
        // when
        groupParticipant.verify(126.9745357, 37.570387);
        assertThat(groupParticipant.getLocationVerificationStatus())
                .isEqualTo(LocationVerificationStatus.VERIFIED);
    }

    @Test
    @DisplayName("이미 인증한 회원은 다시 인증할 수 없다.")
    void duplicatedVerification() {
        // given
        final Member host = TestMemberFactory.create(1L);
        final Group group = TestGroupFactory.createActiveGroup(host);
        final Member member = TestMemberFactory.create(2L);
        final GroupParticipant groupParticipant = TestParticipantsFactory.create(member);
        group.addParticipant(groupParticipant);
        groupParticipant.verify(126.9745357, 37.570387);
        // when&then
        assertThatThrownBy(() -> groupParticipant.verify(126.9745357, 37.570387))
                .isInstanceOf(LocationVerificationException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.VERIFICATION_DUPLICATED);
    }
}
