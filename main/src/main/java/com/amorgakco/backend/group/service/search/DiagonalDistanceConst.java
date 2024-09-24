package com.amorgakco.backend.group.service.search;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiagonalDistanceConst {
    MAX_DISTANCE(14200),
    MIN_DISTANCE(7500);

    private final double value;
}
