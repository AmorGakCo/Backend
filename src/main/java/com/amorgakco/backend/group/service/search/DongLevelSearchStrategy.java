package com.amorgakco.backend.group.service.search;

import com.amorgakco.backend.global.GoogleS2Const;
import com.amorgakco.backend.group.dto.GroupSearchRequest;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2LatLngRect;
import com.google.common.geometry.S2RegionCoverer;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DongLevelSearchStrategy implements GroupSearchStrategy {

    private static final double LATITUDE_SIZE = 0.07;
    private static final double LONGITUDE_SIZE = 0.8;
    private static final double MIN_DISTANCE = 5600;

    @Override
    public boolean isValid(final double diagonalSize) {
        return diagonalSize <= MIN_DISTANCE;
    }

    @Override
    public List<String> getTokens(final GroupSearchRequest request) {
        final S2LatLngRect s2LatLngRect = createRectangle(request);
        final S2RegionCoverer coverer =
                S2RegionCoverer.builder()
                        .setMinLevel(GoogleS2Const.S2_CELL_LEVEL.getValue())
                        .setMaxLevel(GoogleS2Const.S2_CELL_LEVEL.getValue())
                        .build();
        final ArrayList<S2CellId> cellIds = new ArrayList<>();
        coverer.getCovering(s2LatLngRect, cellIds);
        return cellIds.stream().map(S2CellId::toToken).toList();
    }

    private S2LatLngRect createRectangle(final GroupSearchRequest request) {
        return S2LatLngRect.fromCenterSize(
                S2LatLng.fromDegrees(request.centerLat(), request.centerLon()),
                S2LatLng.fromDegrees(LATITUDE_SIZE, LONGITUDE_SIZE));
    }
}
