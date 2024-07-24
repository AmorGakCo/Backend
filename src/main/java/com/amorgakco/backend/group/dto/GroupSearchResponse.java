package com.amorgakco.backend.group.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record GroupSearchResponse(List<GroupLocation> locations) {}
