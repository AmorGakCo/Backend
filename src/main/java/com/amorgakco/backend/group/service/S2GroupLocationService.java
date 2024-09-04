package com.amorgakco.backend.group.service;

import com.amorgakco.backend.global.exception.IllegalAccessException;
import com.amorgakco.backend.group.domain.location.LocationCalculator;
import com.amorgakco.backend.group.dto.GroupSearchRequest;
import com.amorgakco.backend.group.dto.GroupSearchResponse;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.group.service.mapper.GroupMapper;
import com.amorgakco.backend.group.service.search.GroupSearchStrategy;

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
        final double diagonalDistance = getDiagonalDistance(request);
        final GroupSearchStrategy groupSearchStrategy = getStrategy(diagonalDistance);
        final List<String> cellTokens = groupSearchStrategy.selectCellTokens(request);
        return findGroups(cellTokens);
    }

    private GroupSearchStrategy getStrategy(final double diagonalDistance) {
        return searchStrategies.stream()
                .filter(s -> s.isValid(diagonalDistance))
                .findFirst()
                .orElseThrow(IllegalAccessException::invalidDiagonalDistance);
    }

    private double getDiagonalDistance(final GroupSearchRequest request) {
        return LocationCalculator.getDistance(
                request.southWestLon(),
                request.southWestLat(),
                request.northEastLon(),
                request.northEastLon());
    }

    private GroupSearchResponse findGroups(final List<String> cellTokens) {
        return groupRepository.findByCellToken(cellTokens).stream()
                .map(groupMapper::toGroupLocation)
                .collect(
                        Collectors.collectingAndThen(
                                Collectors.toList(), GroupSearchResponse::new));
    }
}
