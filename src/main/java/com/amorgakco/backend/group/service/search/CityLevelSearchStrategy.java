package com.amorgakco.backend.group.service.search;

import com.amorgakco.backend.group.dto.GroupSearchRequest;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2LatLngRect;

import org.springframework.stereotype.Component;

@Component
public class CityLevelSearchStrategy extends GroupSearchStrategy {

    private static final double LATITUDE_SIZE = 0.07;
    private static final double LONGITUDE_SIZE = 0.14;

    @Override
    public boolean isValid(final double diagonalSize) {
        return diagonalSize > DiagonalDistanceConst.MAX_DISTANCE.getValue();
    }

    @Override
    public S2LatLngRect createRectangle(final GroupSearchRequest request) {
        return S2LatLngRect.fromCenterSize(
                S2LatLng.fromDegrees(request.centerLat(), request.centerLon()),
                S2LatLng.fromDegrees(LATITUDE_SIZE, LONGITUDE_SIZE));
    }
}
