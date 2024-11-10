package com.amorgakco.backend.group.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record GroupSearchResponse(List<GroupLocation> locations) {

}
