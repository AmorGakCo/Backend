package com.amorgakco.backend.group.domain;

import static org.assertj.core.api.Assertions.*;

import com.amorgakco.backend.group.domain.location.Location;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LocationTest {

    private static final double LONGITUDE = 126.9748397;
    private static final double LATITUDE = 37.5703901;

    @Test
    @DisplayName("인증 허용 범위 밖에서 위치를 인증하면 true를 반환한다.")
    void isNotInBoundary() {
        // given
        final Location location = new Location(LONGITUDE, LATITUDE);
        final double targetLatitude = 37.57071;
        final double targetLongitude = 126.9754143;
        // when & then
        assertThat(location.isNotInBoundary(targetLongitude, targetLatitude)).isTrue();
    }

    @Test
    @DisplayName("인증 허용 범위 안에서 위치를 인증하면 false를 반환한다.")
    void isInBoundary() {
        // given
        final Location location = new Location(LONGITUDE, LATITUDE);
        final double targetLatitude = 37.570387;
        final double targetLongitude = 126.9745357;
        // when & then
        assertThat(location.isNotInBoundary(targetLongitude, targetLatitude)).isFalse();
    }
}
