package com.amorgakco.backend.group.service;

import com.amorgakco.backend.global.GoogleS2Const;
import com.amorgakco.backend.group.dto.GroupSearchRequest;
import com.amorgakco.backend.group.dto.GroupSearchResponse;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.group.service.mapper.GroupMapper;
import com.amorgakco.backend.group.service.search.GroupSearchStrategy;
import com.google.common.geometry.S2CellId;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2LatLngRect;
import com.google.common.geometry.S2RegionCoverer;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S2GroupLocationService implements GroupLocationService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final List<GroupSearchStrategy> searchStrategies;
    private static final double MIN_LATITUDE_DIFFERENCE = 0.5;
    private static final double MIN_LONGITUDE_DIFFERENCE = 1;

    public GroupSearchResponse getNearByGroups(final GroupSearchRequest request) {
        validateRectangleSize(request);
        final S2LatLngRect s2LatLngRect = getRectangleRegion(request);
        final S2RegionCoverer coverer =
                S2RegionCoverer.builder()
                        .setMinLevel(GoogleS2Const.S2_CELL_LEVEL.getValue())
                        .setMaxLevel(GoogleS2Const.S2_CELL_LEVEL.getValue())
                        .build();
        final ArrayList<S2CellId> cellIds = new ArrayList<>();
        coverer.getCovering(s2LatLngRect, cellIds);
        final List<String> cellTokens = cellIds.stream().map(S2CellId::toToken).toList();
        return findCells(cellTokens);
    }

    private void validateRectangleSize(final GroupSearchRequest request) {
        S2LatLng center = S2LatLng.fromDegrees(request.centerLat(), request.centerLon());
        S2LatLng difference =
                S2LatLng.fromDegrees(MIN_LATITUDE_DIFFERENCE, MIN_LONGITUDE_DIFFERENCE);
        S2LatLngRect s2LatLngRect = S2LatLngRect.fromCenterSize(center, difference);
    }

    private S2LatLngRect getRectangleRegion(final GroupSearchRequest request) {
        return S2LatLngRect.fromPointPair(
                S2LatLng.fromDegrees(request.southWestLat(), request.southWestLon()),
                S2LatLng.fromDegrees(request.northEastLat(), request.northEastLon()));
    }

    private GroupSearchResponse findCells(final List<String> cellTokens) {
        return groupRepository.findByCellToken(cellTokens).stream()
                .map(groupMapper::toGroupLocation)
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toList(), GroupSearchResponse::new));
    }
}
