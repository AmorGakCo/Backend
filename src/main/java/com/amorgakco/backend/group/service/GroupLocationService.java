package com.amorgakco.backend.group.service;

import com.amorgakco.backend.group.dto.GroupSearchRequest;
import com.amorgakco.backend.group.dto.GroupSearchResponse;

public interface GroupLocationService {
    GroupSearchResponse getNearByGroups(final GroupSearchRequest request);
}
