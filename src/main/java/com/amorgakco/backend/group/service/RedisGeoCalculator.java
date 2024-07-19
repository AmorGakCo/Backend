package com.amorgakco.backend.group.service;

import com.amorgakco.backend.global.exception.IllegalAccessException;
import com.amorgakco.backend.group.dto.GroupLocation;
import com.amorgakco.backend.group.service.mapper.GeoSearchMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.domain.geo.BoundingBox;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class RedisGeoCalculator implements GeoCalculator {

    private static final String AMOR_GAK_CO = "amor_gak_co";
    private final GeoOperations<String, String> geoOperations;
    private final GeoSearchMapper geoSearchMapper;

    @Override
    public void save(final String member, final double longitude, final double latitude) {
        final Point point = new Point(longitude, latitude);
        geoOperations.add(AMOR_GAK_CO, point, member);
    }

    @Override
    public List<GroupLocation> searchByBox(
            final double width,
            final double height,
            final double longitude,
            final double latitude) {
        final GeoReference<String> geoReference = GeoReference.fromCoordinate(longitude, latitude);
        final BoundingBox boundingBox = new BoundingBox(width, height, Metrics.KILOMETERS);
        final GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                getResults(geoReference, boundingBox);
        return getLocations(results);
    }

    private GeoResults<RedisGeoCommands.GeoLocation<String>> getResults(
            final GeoReference<String> geoReference, final BoundingBox boundingBox) {
        return geoOperations.search(
                AMOR_GAK_CO,
                geoReference,
                boundingBox,
                RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs().includeCoordinates());
    }

    private List<GroupLocation> getLocations(
            final GeoResults<RedisGeoCommands.GeoLocation<String>> results) {
        return results.getContent().stream()
                .map(GeoResult::getContent)
                .map(c -> geoSearchMapper.toGroupLocation(c.getPoint(), c.getName()))
                .toList();
    }

    @Override
    public List<GroupLocation> searchByCircle(
            final double radius, final double longitude, final double latitude)
            throws IllegalAccessException {
        final Point point = new Point(longitude, latitude);
        final Distance distance = new Distance(radius, Metrics.KILOMETERS);
        final Circle circle = new Circle(point, distance);
        final GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                geoOperations.radius(
                        AMOR_GAK_CO,
                        circle,
                        RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                                .includeCoordinates());
        return getLocations(results);
    }
}
