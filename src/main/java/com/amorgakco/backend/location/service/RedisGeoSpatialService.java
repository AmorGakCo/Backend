package com.amorgakco.backend.location.service;

import com.amorgakco.backend.location.dto.GroupLocation;
import com.amorgakco.backend.location.dto.GroupLocationRequest;
import com.amorgakco.backend.location.dto.GroupLocationResponse;
import com.amorgakco.backend.location.service.mapper.GroupLocationMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.domain.geo.BoundingBox;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RedisGeoSpatialService implements GeoSpatialService {

    private static final String AMOR_GAK_CO = "amor_gak_co";
    private final GeoOperations<String, String> geoOperations;
    private final GroupLocationMapper groupLocationMapper;

    public void save(final Long groupId, final double latitude, final double longitude) {
        final Point point = new Point(longitude, latitude);
        geoOperations.add(AMOR_GAK_CO, point, groupId.toString());
    }

    public GroupLocationResponse getNearByGroups(final GroupLocationRequest locationRequest) {
        final GeoReference<String> geoReference =
                GeoReference.fromCoordinate(
                        locationRequest.longitude(), locationRequest.latitude());
        final BoundingBox boundingBox =
                new BoundingBox(
                        locationRequest.width(), locationRequest.height(), Metrics.KILOMETERS);
        final GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                geoOperations.search(AMOR_GAK_CO, geoReference, boundingBox);
        final List<GroupLocation> locations = getLocations(results);
        return new GroupLocationResponse(locations);
    }

    private List<GroupLocation> getLocations(
            final GeoResults<RedisGeoCommands.GeoLocation<String>> results) {
        return results.getContent().stream()
                .map(GeoResult::getContent)
                .map(a -> groupLocationMapper.toGroupLocation(a.getPoint(), a.getName()))
                .toList();
    }
}
