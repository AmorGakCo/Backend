package com.amorgakco.backend.group.dto;

public record LocationSearchRequest(
        double southWestLat,
        double southWestLon,
        double northEastLat,
        double northEastLon,
        double centerLat,
        double centerLon) {}
