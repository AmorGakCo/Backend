package com.amorgakco.backend.member.domain;

import static org.assertj.core.api.Assertions.*;

import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.global.exception.IllegalFormatException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

class MemberTest {

    @Test
    @DisplayName("깃헙 주소, sms 수신여부, 전화번호, 위치 정보를 갱신할 수 있다.")
    void updateAddtionalInfo() {
        // given
        final Member member = TestMemberFactory.create(1L);
        final GeometryFactory geometryFactory = new GeometryFactory();
        final Point point = geometryFactory.createPoint(new Coordinate(128.3245, 37.3243));
        // when
        member.validateAndUpdateAdditionalInfo("github.com/song", "01011112222", true, point);
        // then
        assertThat(member.getLocation().getX()).isEqualTo(128.3245);
        assertThat(member.getLocation().getY()).isEqualTo(37.3243);
        assertThat(member.getGithubUrl()).isEqualTo("github.com/song");
        assertThat(member.getPhoneNumber()).isEqualTo("01011112222");
    }

    @Test
    @DisplayName("github 주소가 아니라면 예외가 발생한다.")
    void validateGithubAddress() {
        // given
        final Member member = TestMemberFactory.create(1L);
        final GeometryFactory geometryFactory = new GeometryFactory();
        final Point point = geometryFactory.createPoint(new Coordinate(128.3245, 37.3243));
        // when & then
        assertThatThrownBy(
                        () ->
                                member.validateAndUpdateAdditionalInfo(
                                        "invalid.com", "01011112222", true, point))
                .isInstanceOf(IllegalFormatException.class);
    }

    @Test
    @DisplayName("대쉬를 포함한 전화번호는 예외를 발생시킨다.")
    void validatePhoneNumber() {
        // given
        final Member member = TestMemberFactory.create(1L);
        final GeometryFactory geometryFactory = new GeometryFactory();
        final Point point = geometryFactory.createPoint(new Coordinate(128.3245, 37.3243));
        // when & then
        assertThatThrownBy(
                        () ->
                                member.validateAndUpdateAdditionalInfo(
                                        "invalid.com", "010-1111-2222", true, point))
                .isInstanceOf(IllegalFormatException.class);
    }
}
