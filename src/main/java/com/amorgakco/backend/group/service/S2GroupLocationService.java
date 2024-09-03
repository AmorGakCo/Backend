package com.amorgakco.backend.group.service;

import com.amorgakco.backend.group.domain.location.LocationCalculator;
import com.amorgakco.backend.group.dto.GroupSearchRequest;
import com.amorgakco.backend.group.dto.GroupSearchResponse;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.group.service.mapper.GroupMapper;
import com.amorgakco.backend.group.service.search.GroupSearchStrategy;
import com.google.common.geometry.S2CellId;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S2GroupLocationService implements GroupLocationService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final List<GroupSearchStrategy> searchStrategies;

    public GroupSearchResponse getNearByGroups(final GroupSearchRequest request) {
        final double diagonalSize = getDiagonalSize(request);
        getStrategy();
        coverer.getCovering(s2LatLngRect, cellIds);
        final List<String> cellTokens = cellIds.stream().map(S2CellId::toToken).toList();
        return findCells(cellTokens);
    }

    private GroupSearchStrategy getStrategy(final double diagonalSize) {
        searchStrategies.stream().filter(s -> s.isValid(diagonalSize)).findFirst().orElseGet();
    }

    private double getDiagonalSize(final GroupSearchRequest request) {
        return LocationCalculator.getDistance(
                request.southWestLon(),
                request.southWestLat(),
                request.northEastLon(),
                request.northEastLon());
    }

    private GroupSearchResponse findCells(final List<String> cellTokens) {
        return groupRepository.findByCellToken(cellTokens).stream()
                .map(groupMapper::toGroupLocation)
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toList(), GroupSearchResponse::new));
    }
}
