package com.amorgakco.backend.grouplocation.dto;

import lombok.Builder;

@Builder
public record GroupLocation(double latitude, double longitude, String groupId) {}
