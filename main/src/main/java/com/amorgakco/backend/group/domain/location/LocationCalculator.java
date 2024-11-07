package com.amorgakco.backend.group.domain.location;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationCalculator {

    private static final double EARTH_RADIUS = 6371;

    public static double getDistance(
        final double sourceLongitude,
        final double sourceLatitude,
        final double targetLongitude,
        final double targetLatitude) {
        final double deltaLatitude = Math.toRadians(sourceLatitude - targetLatitude);
        final double deltaLongitude = Math.toRadians(sourceLongitude - targetLongitude);

        final double haversine =
            Math.sin(deltaLatitude / 2) * Math.sin(deltaLatitude / 2)
                + Math.cos(Math.toRadians(sourceLatitude))
                * Math.cos(Math.toRadians(targetLatitude))
                * Math.sin(deltaLongitude / 2)
                * Math.sin(deltaLatitude / 2);
        return EARTH_RADIUS
            * 2
            * Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine))
            * 1000; // Distance in m
    }
}
