package com.amorgakco.backend.grouplocation.service;

import com.amorgakco.backend.grouplocation.dto.GroupLocationRequest;
import com.amorgakco.backend.grouplocation.dto.GroupLocationResponse;

public interface GeoSpatialService {
    void save(final Long groupId, final double latitude, final double longitude);

    GroupLocationResponse getNearByGroups(final GroupLocationRequest locationRequest);
}
