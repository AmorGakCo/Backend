package com.amorgakco.backend.group.domain;

import static org.assertj.core.api.Assertions.*;

import com.amorgakco.backend.fixture.group.TestLocationFactory;
import com.amorgakco.backend.group.domain.location.Location;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LocationTest {

    @Test
    @DisplayName("인증 허용 범위 밖에서 위치를 인증하면 true를 반환한다.")
    void isNotInBoundary() {
        // given
        final Location location = TestLocationFactory.create();
        // when & then
        assertThat(location.isNotInBoundary(126.9754143, 37.57071)).isTrue();
    }

    @Test
    @DisplayName("인증 허용 범위 안에서 위치를 인증하면 false를 반환한다.")
    void isInBoundary() {
        // given
        final Location location = TestLocationFactory.create();
        // when & then
        assertThat(location.isNotInBoundary(126.9745357, 37.570387)).isFalse();
    }

    @Test
    @DisplayName("최대 탐색 허용 반경을 초과하면 3000m를 반환한다.")
    void isNotInSearchBoundary() {
        // given
        final double requestRadius = 4000;
        final Location location = TestLocationFactory.create();
        // when & then
        assertThat(location.validateAndGetRadius(requestRadius)).isEqualTo(3000);
    }

    @Test
    @DisplayName("최대 탐색 허용 반경을 초과하지 않으면 요청한 값을 반환한다.")
    void isInSearchBoundary() {
        // given
        final double requestRadius = 2500;
        final Location location = TestLocationFactory.create();
        // when & then
        assertThat(location.validateAndGetRadius(requestRadius)).isEqualTo(2500);
    }
}
