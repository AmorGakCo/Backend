package com.amorgakco.backend.member.domain;

import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.global.exception.IllegalFormatException;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2Point;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    @Test
    @DisplayName("깃헙 주소, sms 수신여부, 전화번호, 위치 정보를 갱신할 수 있다.")
    void updateAddtionalInfo() {
        // given
        final Member member = TestMemberFactory.create(1L);
        final S2Point point = S2LatLng.fromDegrees(37.3243, 128.3245).toPoint();
        final String cellToken = S2CellId.fromPoint(point).parent(14).toToken();
        // when
        member.validateAndUpdateAdditionalInfo("github.com/song", "01011112222", true, cellToken);
        // then
        assertThat(member.getCellToken()).isEqualTo(cellToken);
        assertThat(member.getGithubUrl()).isEqualTo("github.com/song");
        assertThat(member.getPhoneNumber()).isEqualTo("01011112222");
    }

    @Test
    @DisplayName("github 주소가 아니라면 예외가 발생한다.")
    void validateGithubAddress() {
        // given
        final Member member = TestMemberFactory.create(1L);
        final S2Point point = S2LatLng.fromDegrees(37.3243, 128.3245).toPoint();
        final String cellToken = S2CellId.fromPoint(point).parent(14).toToken();
        // when & then
        assertThatThrownBy(
                () ->
                        member.validateAndUpdateAdditionalInfo(
                                "invalid.com", "01011112222", true, cellToken))
                .isInstanceOf(IllegalFormatException.class);
    }

    @Test
    @DisplayName("대쉬를 포함한 전화번호는 예외를 발생시킨다.")
    void validatePhoneNumber() {
        // given
        final Member member = TestMemberFactory.create(1L);
        final S2Point point = S2LatLng.fromDegrees(37.3243, 128.3245).toPoint();
        final String cellToken = S2CellId.fromPoint(point).parent(14).toToken();
        // when & then
        assertThatThrownBy(
                () ->
                        member.validateAndUpdateAdditionalInfo(
                                "invalid.com", "010-1111-2222", true, cellToken))
                .isInstanceOf(IllegalFormatException.class);
    }
}
