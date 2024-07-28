package com.amorgakco.backend.group.domain.location;

import org.springframework.stereotype.Component;

@Component
public class HaversineCalculator implements LocationCalculator {

    private static final double EARTH_RADIUS = 6371;
    private static final double TO_RADIAN = Math.PI / 180;

    @Override
    public double getDistance(
            final double sourceLongitude,
            final double sourceLatitude,
            final double targetLongitude,
            final double targetLatitude) {
        final double deltaLatitude = Math.abs(sourceLatitude - targetLatitude) * TO_RADIAN;
        final double deltaLongitude = Math.abs(sourceLongitude - targetLongitude) * TO_RADIAN;

        final double sinDeltaLat = Math.sin(deltaLatitude / 2);
        final double sinDeltaLng = Math.sin(deltaLongitude / 2);
        final double squareRoot =
                Math.sqrt(
                        sinDeltaLat * sinDeltaLat
                                + Math.cos(sourceLatitude * TO_RADIAN)
                                        * Math.cos(
                                                targetLatitude
                                                        * TO_RADIAN
                                                        * sinDeltaLng
                                                        * sinDeltaLng));

        return 2 * EARTH_RADIUS * Math.asin(squareRoot);
    }
}
