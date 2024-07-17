package com.amorgakco.backend.geospatial.dto;

import lombok.Builder;

@Builder
public record GroupGeospatial(double latitude, double longitude, String groupId) {}
