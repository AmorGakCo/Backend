package com.amorgakco.backend.location.service;

import com.amorgakco.backend.location.dto.GroupLocationRequest;
import com.amorgakco.backend.location.dto.GroupLocationResponse;

public interface GeoSpatialService {
    void save(final Long groupId, final double latitude, final double longitude);

    GroupLocationResponse getNearByGroups(final GroupLocationRequest locationRequest);
}
