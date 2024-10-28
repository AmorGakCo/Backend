package com.amorgakco.backend.group.service.search;

import com.amorgakco.backend.global.exception.GroupSearchException;
import com.amorgakco.backend.group.domain.location.LocationCalculator;
import com.amorgakco.backend.group.dto.GroupSearchRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class S2CellSearch {

    private final List<GroupSearchStrategy> searchStrategies;

    public List<String> getCellTokens(final GroupSearchRequest request) {
        final double diagonalDistance = getDiagonalDistance(request);
        final GroupSearchStrategy groupSearchStrategy = getSearchStrategy(diagonalDistance);
        return groupSearchStrategy.selectCellTokens(request);
    }

    private double getDiagonalDistance(final GroupSearchRequest request) {
        return LocationCalculator.getDistance(
                request.southWestLon(),
                request.southWestLat(),
                request.northEastLon(),
                request.northEastLat());
    }

    private GroupSearchStrategy getSearchStrategy(final double diagonalDistance) {
        return searchStrategies.stream()
                .filter(s -> s.isValid(diagonalDistance))
                .findFirst()
                .orElseThrow(GroupSearchException::invalidDiagonalDistance);
    }
}
