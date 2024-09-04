package com.amorgakco.backend.group.service.search;

import org.springframework.stereotype.Component;

@Component
public class GuLevelSearchStrategy extends GroupSearchStrategy {

    private static final double MAX_DISTANCE = 14200;
    private static final double MIN_DISTANCE = 7500;

    @Override
    public boolean isValid(final double diagonalSize) {
        return DiagonalDistanceConst.MIN_DISTANCE.getValue() < diagonalSize
                && diagonalSize <= DiagonalDistanceConst.MAX_DISTANCE.getValue();
    }
}
