package com.amorgakco.backend.geospatial.service;

import com.amorgakco.backend.geospatial.dto.GeoSpatialRequest;
import com.amorgakco.backend.geospatial.dto.GeoSpatialResponse;
import com.amorgakco.backend.geospatial.dto.GroupGeoSpatial;
import com.amorgakco.backend.geospatial.service.mapper.GeoSpatialMapper;

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
    private final GeoSpatialMapper geoSpatialMapper;

    public void save(final Long groupId, final double latitude, final double longitude) {
        final Point point = new Point(longitude, latitude);
        geoOperations.add(AMOR_GAK_CO, point, groupId.toString());
    }

    public GeoSpatialResponse getNearByGroups(final GeoSpatialRequest request) {
        final GeoReference<String> geoReference =
                GeoReference.fromCoordinate(request.longitude(), request.latitude());
        final BoundingBox boundingBox =
                new BoundingBox(request.width(), request.height(), Metrics.KILOMETERS);
        final GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                getResults(geoReference, boundingBox);
        final List<GroupGeoSpatial> locations = getLocations(results);
        return new GeoSpatialResponse(locations);
    }

    private GeoResults<RedisGeoCommands.GeoLocation<String>> getResults(
            final GeoReference<String> geoReference, final BoundingBox boundingBox) {
        return geoOperations.search(
                AMOR_GAK_CO,
                geoReference,
                boundingBox,
                RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs().includeCoordinates());
    }

    private List<GroupGeoSpatial> getLocations(
            final GeoResults<RedisGeoCommands.GeoLocation<String>> results) {
        return results.getContent().stream()
                .map(GeoResult::getContent)
                .map(a -> geoSpatialMapper.toGroupGeoSpatial(a.getPoint(), a.getName()))
                .toList();
    }
}
