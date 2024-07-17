package com.amorgakco.backend.geospatial.service;

import com.amorgakco.backend.geospatial.dto.GeospatialRequest;
import com.amorgakco.backend.geospatial.dto.GeospatialResponse;

public interface GeospatialService {
    void save(final Long groupId, final double latitude, final double longitude);

    GeospatialResponse getNearByGroups(final GeospatialRequest locationRequest);
}
