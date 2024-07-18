package com.amorgakco.backend.geospatial.controller;

import com.amorgakco.backend.geospatial.dto.GeospatialRequest;
import com.amorgakco.backend.geospatial.dto.GeospatialResponse;
import com.amorgakco.backend.geospatial.service.GeospatialService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class GeospatialController {

    private final GeospatialService geoSpatialService;

    @GetMapping
    public GeospatialResponse getLocations(
            @RequestParam final double latitude,
            @RequestParam final double longitude,
            @RequestParam final double width,
            @RequestParam final double height) {
        return geoSpatialService.getNearByGroups(
                new GeospatialRequest(width, height, latitude, longitude));
    }
}
