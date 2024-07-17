package com.amorgakco.backend.geospatial.dto;

import lombok.Builder;

@Builder
public record GroupGeoSpatial(double latitude, double longitude, String groupId) {}
