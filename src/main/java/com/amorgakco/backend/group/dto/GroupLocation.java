package com.amorgakco.backend.group.dto;

import lombok.Builder;

@Builder
public record GroupLocation(double latitude, double longitude, Long groupId) {}
