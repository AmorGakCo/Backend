package com.amorgakco.backend.group.service.search;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiagonalDistanceConst {
    MAX_DISTANCE(14200),
    MIN_DISTANCE(7500);

    private final double value;
}
