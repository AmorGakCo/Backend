package com.amorgakco.backend.group.service;

import com.amorgakco.backend.group.dto.GroupSearchResponse;
import com.amorgakco.backend.group.dto.LocationSearchRequest;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.group.service.mapper.GroupMapper;
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

    private static final int S2_CELL_LEVEL = 14;
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public GroupSearchResponse getNearByGroups(final LocationSearchRequest request) {
        final S2LatLngRect s2LatLngRect = getRectangleRegion(request);
        final S2RegionCoverer coverer =
                S2RegionCoverer.builder()
                        .setMinLevel(S2_CELL_LEVEL)
                        .setMaxLevel(S2_CELL_LEVEL)
                        .build();
        final ArrayList<S2CellId> cellIds = new ArrayList<>();
        coverer.getCovering(s2LatLngRect, cellIds);
        final List<String> cellTokens = cellIds.stream().map(S2CellId::toToken).toList();
        return findCells(cellTokens);
    }

    private GroupSearchResponse findCells(final List<String> cellTokens) {
        return groupRepository.findByCellToken(cellTokens).stream()
                .map(groupMapper::toGroupLocation)
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toList(), GroupSearchResponse::new));
    }

    private S2LatLngRect getRectangleRegion(final LocationSearchRequest request) {
        return S2LatLngRect.fromPointPair(
                S2LatLng.fromDegrees(request.southWestLat(), request.southWestLon()),
                S2LatLng.fromDegrees(request.northEastLat(), request.northEastLon()));
    }
}
