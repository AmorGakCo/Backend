package com.amorgakco.backend.fixture.group;

import com.amorgakco.backend.group.domain.location.Location;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

public class TestLocationFactory {

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public static Location create() {
        geometryFactory.createPoint();
        return new Location(geometryFactory.createPoint(new Coordinate(126.9748397, 37.5703901)));
    }
}
