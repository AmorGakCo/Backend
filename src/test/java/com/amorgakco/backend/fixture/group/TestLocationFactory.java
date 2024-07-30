package com.amorgakco.backend.fixture.group;

import com.amorgakco.backend.group.domain.location.Location;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

public class TestLocationFactory {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public static Location create(final double longitude, final double latitude) {
        geometryFactory.createPoint();
        return new Location(geometryFactory.createPoint(new Coordinate(longitude, latitude)));
    }
}
