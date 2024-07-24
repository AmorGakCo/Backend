package com.amorgakco.backend.group.domain;

import com.amorgakco.backend.global.exception.IllegalAccessException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.locationtech.jts.awt.PointShapeFactory;
import org.locationtech.jts.geom.Point;

import java.awt.*;
import java.awt.geom.Point2D;

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

    public void verify(final double longitude, final double latitude) {
        final Point2D.Double groupPoint = new Point2D.Double(this.point.getX(), this.point.getY());
        final PointShapeFactory.Circle circle =
                new PointShapeFactory.Circle(VERIFICATION_RADIUS_LIMIT);
        final Shape groupCircle = circle.createPoint(groupPoint);
        validateCurrentLocation(longitude, latitude, groupCircle);
    }

    private void validateCurrentLocation(
            final double longitude, final double latitude, final Shape groupCircle) {
        if (isNotInCircle(longitude, latitude, groupCircle)) {
            throw IllegalAccessException.verificationFailed();
        }
    }

    private boolean isNotInCircle(
            final double longitude, final double latitude, final Shape groupCircle) {
        return !groupCircle.contains(longitude, latitude);
    }

    public double validateAndGetRadius(final double radius) {
        return Math.min(radius, VALID_RADIUS_LIMIT);
    }
}
