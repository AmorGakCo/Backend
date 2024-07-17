package com.amorgakco.backend.group.controller;

import com.amorgakco.backend.global.CommonIdResponse;
import com.amorgakco.backend.global.config.argumentresolver.AuthMember;
import com.amorgakco.backend.group.dto.GroupBasicInfoResponse;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.group.service.GroupService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public CommonIdResponse register(
            @RequestBody final GroupRegisterRequest groupRegisterRequest,
            @AuthMember final Long hostId) {
        return groupService.register(groupRegisterRequest, hostId);
    }

    @GetMapping("/basic/{groupId}")
    public GroupBasicInfoResponse getGroupInfo(@PathVariable final Long groupId) {
        return groupService.getGroupInfo(groupId);
    }
}
