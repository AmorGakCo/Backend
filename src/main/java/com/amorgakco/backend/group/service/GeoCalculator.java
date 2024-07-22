package com.amorgakco.backend.group.service;

import com.amorgakco.backend.group.dto.GroupLocation;

import java.util.List;

public interface GeoCalculator {
    void save(final String member, final double longitude, final double latitude);

    List<GroupLocation> searchByBox(
            final double width, final double height, final double longitude, final double latitude);

    List<GroupLocation> searchByCircle(
            final double radius, final double longitude, final double latitude);
}
