package com.amorgakco.backend.group.domain.location;

public interface LocationCalculator {
    double getDistance(
            final double sourceLongitude,
            final double sourceLatitude,
            final double targetLongitude,
            final double targetLatitude);
}
