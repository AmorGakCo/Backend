package com.amorgakco.backend.group.domain.location;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.locationtech.jts.geom.Point;

import java.awt.*;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location {
    // 위치 인증 허용 반경 : 50m
    private static final double VERIFICATION_RADIUS_LIMIT = 50;
    // 탐색 허용 반경 : 3000m
    private static final double VALID_RADIUS_LIMIT = 3000;

    @Column(columnDefinition = "geometry(POINT, 4326)", name = "point")
    private Point point;

    public Location(final Point point) {
        this.point = point;
    }

    public void verify(final double longitude, final double latitude) {}

    public double validateAndGetRadius(final double radius) {
        return Math.min(radius, VALID_RADIUS_LIMIT);
    }
}
