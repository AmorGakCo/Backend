package com.amorgakco.backend.geospatial.service;

import com.amorgakco.backend.geospatial.dto.GeospatialRequest;
import com.amorgakco.backend.geospatial.dto.GeospatialResponse;
import com.amorgakco.backend.geospatial.dto.GroupGeospatial;
import com.amorgakco.backend.geospatial.service.mapper.GeospatialMapper;

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
public class RedisGeospatialService implements GeospatialService {

    private static final String AMOR_GAK_CO = "amor_gak_co";
    private final GeoOperations<String, String> geoOperations;
    private final GeospatialMapper geoSpatialMapper;

    public void save(final Long groupId, final double latitude, final double longitude) {
        final Point point = new Point(longitude, latitude);
        geoOperations.add(AMOR_GAK_CO, point, groupId.toString());
    }

    public GeospatialResponse getNearByGroups(final GeospatialRequest request) {
        final GeoReference<String> geoReference =
                GeoReference.fromCoordinate(request.longitude(), request.latitude());
        final BoundingBox boundingBox =
                new BoundingBox(request.width(), request.height(), Metrics.KILOMETERS);
        final GeoResults<RedisGeoCommands.GeoLocation<String>> results =
                getResults(geoReference, boundingBox);
        final List<GroupGeospatial> locations = getLocations(results);
        return new GeospatialResponse(locations);
    }

    private GeoResults<RedisGeoCommands.GeoLocation<String>> getResults(
            final GeoReference<String> geoReference, final BoundingBox boundingBox) {
        return geoOperations.search(
                AMOR_GAK_CO,
                geoReference,
                boundingBox,
                RedisGeoCommands.GeoSearchCommandArgs.newGeoSearchArgs().includeCoordinates());
    }

    private List<GroupGeospatial> getLocations(
            final GeoResults<RedisGeoCommands.GeoLocation<String>> results) {
        return results.getContent().stream()
                .map(GeoResult::getContent)
                .map(a -> geoSpatialMapper.toGroupGeoSpatial(a.getPoint(), a.getName()))
                .toList();
    }
}
