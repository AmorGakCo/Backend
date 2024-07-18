package com.amorgakco.backend.group.controller;

import com.amorgakco.backend.global.CommonIdResponse;
import com.amorgakco.backend.global.argumentresolver.AuthMember;
import com.amorgakco.backend.group.dto.GroupBasicInfoResponse;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.group.service.GroupService;

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
    public CommonIdResponse register(
            @RequestBody final GroupRegisterRequest groupRegisterRequest,
            @AuthMember final Long hostId) {
        return groupService.register(groupRegisterRequest, hostId);
    }

    @GetMapping("/basic/{groupId}")
    public GroupBasicInfoResponse getGroupBasicInfo(@PathVariable final Long groupId) {
        return groupService.getBasicGroupInfo(groupId);
    }

    @DeleteMapping("/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthMember final Long hostId, @PathVariable final Long groupId) {
        groupService.delete(hostId, groupId);
    }
}
