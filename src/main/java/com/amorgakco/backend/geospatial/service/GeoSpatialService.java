package com.amorgakco.backend.geospatial.service;

import com.amorgakco.backend.geospatial.dto.GeoSpatialRequest;
import com.amorgakco.backend.geospatial.dto.GeoSpatialResponse;

public interface GeoSpatialService {
    void save(final Long groupId, final double latitude, final double longitude);

    GeoSpatialResponse getNearByGroups(final GeoSpatialRequest locationRequest);
}
