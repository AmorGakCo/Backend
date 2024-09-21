package com.amorgakco.backend.group.controller;

import com.amorgakco.backend.group.dto.GroupSearchRequest;
import com.amorgakco.backend.group.dto.GroupSearchResponse;
import com.amorgakco.backend.group.service.GroupLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupLocationController {

    private final GroupLocationService groupLocationService;

    @GetMapping("/locations")
    public GroupSearchResponse getLocations(
            @ModelAttribute final GroupSearchRequest groupSearchRequest) {
        return groupLocationService.findGroups(groupSearchRequest);
    }
}
