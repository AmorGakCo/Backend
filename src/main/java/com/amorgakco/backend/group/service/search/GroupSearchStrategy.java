package com.amorgakco.backend.group.service.search;

import com.amorgakco.backend.global.GoogleS2Const;
import com.amorgakco.backend.group.dto.GroupSearchRequest;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2LatLngRect;
import com.google.common.geometry.S2RegionCoverer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public abstract class GroupSearchStrategy {
    public abstract boolean isValid(double diagonalSize);

    public S2LatLngRect createRectangle(final GroupSearchRequest request) {
        return S2LatLngRect.fromPointPair(
                S2LatLng.fromDegrees(request.southWestLat(), request.southWestLon()),
                S2LatLng.fromDegrees(request.northEastLat(), request.northEastLon()));
    }

    public final List<String> getCoveringCells(final GroupSearchRequest request) {
        final S2LatLngRect rectangle = createRectangle(request);
        final S2RegionCoverer coverer =
                S2RegionCoverer.builder()
                        .setMinLevel(GoogleS2Const.S2_CELL_LEVEL.getValue())
                        .setMaxLevel(GoogleS2Const.S2_CELL_LEVEL.getValue())
                        .build();
        final ArrayList<S2CellId> cellIds = new ArrayList<>();
        coverer.getCovering(rectangle, cellIds);
        return cellIds.stream().map(S2CellId::toToken).toList();
    }

    public List<String> selectCellTokens(final GroupSearchRequest request) {
        final List<String> cellTokens = getCoveringCells(request);
        return IntStream.range(0, cellTokens.size() - 1)
                .filter(i -> i % 2 == 0)
                .mapToObj(cellTokens::get)
                .toList();
    }
}
