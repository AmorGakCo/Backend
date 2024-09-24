package com.amorgakco.backend.group.dto;

import lombok.Builder;

@Builder
public record GroupSearchRequest(
        double southWestLat,
        double southWestLon,
        double northEastLat,
        double northEastLon,
        double centerLat,
        double centerLon) {
}
