package com.amorgakco.backend.location.controller;

import com.amorgakco.backend.location.dto.GroupLocationRequest;
import com.amorgakco.backend.location.dto.GroupLocationResponse;
import com.amorgakco.backend.location.service.GeoSpatialService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class GroupLocationController {

    private final GeoSpatialService geoSpatialService;

    @GetMapping
    public GroupLocationResponse getLocations(
            @RequestBody final GroupLocationRequest locationRequest) {
        return geoSpatialService.getNearByGroups(locationRequest);
    }
}
