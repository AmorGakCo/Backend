package com.amorgakco.backend.geospatial.controller;

import com.amorgakco.backend.geospatial.dto.GeoSpatialRequest;
import com.amorgakco.backend.geospatial.dto.GeoSpatialResponse;
import com.amorgakco.backend.geospatial.service.GeoSpatialService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class GeoSpatialController {

    private final GeoSpatialService geoSpatialService;

    @GetMapping
    public GeoSpatialResponse getLocations(
            @RequestParam final double latitude,
            @RequestParam final double longitude,
            @RequestParam final double width,
            @RequestParam final double height) {
        return geoSpatialService.getNearByGroups(
                new GeoSpatialRequest(width, height, latitude, longitude));
    }
}
