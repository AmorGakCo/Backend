package com.amorgakco.backend.group.service.search;

import org.springframework.stereotype.Component;

@Component
public class GuLevelSearchStrategy extends GroupSearchStrategy {

    @Override
    public boolean isValid(final double diagonalSize) {
        return DiagonalDistanceConst.MIN_DISTANCE.getValue() < diagonalSize
                && diagonalSize <= DiagonalDistanceConst.MAX_DISTANCE.getValue();
    }
}
