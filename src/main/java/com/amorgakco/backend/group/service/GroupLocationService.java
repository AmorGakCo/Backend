package com.amorgakco.backend.group.service;

import com.amorgakco.backend.group.dto.GroupSearchResponse;
import com.amorgakco.backend.group.dto.LocationSearchRequest;

public interface GroupLocationService {
    GroupSearchResponse getNearByGroups(final LocationSearchRequest request);
}
