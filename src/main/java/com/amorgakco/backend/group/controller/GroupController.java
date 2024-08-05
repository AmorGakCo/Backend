package com.amorgakco.backend.group.controller;

import com.amorgakco.backend.global.IdResponse;
import com.amorgakco.backend.global.argumentresolver.AuthMember;
import com.amorgakco.backend.global.argumentresolver.AuthMemberId;
import com.amorgakco.backend.group.dto.GroupBasicResponse;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.group.dto.GroupSearchResponse;
import com.amorgakco.backend.group.dto.LocationVerificationRequest;
import com.amorgakco.backend.group.service.GroupService;
import com.amorgakco.backend.member.domain.Member;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IdResponse register(
            @RequestBody final GroupRegisterRequest groupRegisterRequest,
            @AuthMember final Member host) {
        final IdResponse register = groupService.register(groupRegisterRequest, host);
        return register;
    }

    @GetMapping("/basic/{groupId}")
    public GroupBasicResponse getGroupBasicInfo(@PathVariable final Long groupId) {
        return groupService.getBasicGroup(groupId);
    }

    @DeleteMapping("/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthMemberId final Long hostId, @PathVariable final Long groupId) {
        groupService.delete(hostId, groupId);
    }

    @GetMapping("/locations")
    public GroupSearchResponse getLocations(
            @RequestParam final double longitude,
            @RequestParam final double latitude,
            @RequestParam final double radius) {
        return groupService.getNearByGroups(longitude, latitude, radius);
    }

    @PatchMapping("/participants")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verifyLocation(
            @AuthMemberId final Long memberId,
            @RequestBody final LocationVerificationRequest request) {
        groupService.verifyParticipantLocation(request, memberId);
    }
}
