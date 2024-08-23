package com.amorgakco.backend.group.domain.location;

import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2Point;

import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {
    // 위치 인증 허용 반경 : 50m
    private static final double VERIFICATION_RADIUS_LIMIT = 50;
    // 탐색 허용 반경 : 3000m
    private static final double VALID_RADIUS_LIMIT = 3000;

    private String cellToken;
    private double longitude;
    private double latitude;

    public Location(final double longitude, final double latitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        final S2Point point = S2LatLng.fromDegrees(latitude, longitude).toPoint();
        this.cellToken = S2CellId.fromPoint(point).toToken();
    }

    public boolean isNotInBoundary(final double longitude, final double latitude) {
        final double distance =
                LocationCalculator.getDistance(this.longitude, this.latitude, longitude, latitude);
        return distance > VERIFICATION_RADIUS_LIMIT;
    }

    public double validateAndGetRadius(final double radius) {
        return Math.min(radius, VALID_RADIUS_LIMIT);
    }
}
