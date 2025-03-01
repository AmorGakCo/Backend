package com.amorgakco.backend.group.controller;

import com.amorgakco.backend.global.argumentresolver.AuthMember;
import com.amorgakco.backend.global.argumentresolver.AuthMemberId;
import com.amorgakco.backend.group.dto.GroupBasicResponse;
import com.amorgakco.backend.group.dto.GroupDeleteResponse;
import com.amorgakco.backend.group.dto.GroupDetailResponse;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.group.dto.GroupRegisterResponse;
import com.amorgakco.backend.group.service.GroupService;
import com.amorgakco.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GroupRegisterResponse register(
        @RequestBody final GroupRegisterRequest groupRegisterRequest,
        @AuthMember final Member host) {
        return groupService.register(groupRegisterRequest, host);
    }

    @GetMapping("/{groupId}/basic")
    public GroupBasicResponse getGroupBasic(
        @PathVariable final Long groupId, @AuthMember final Member member) {
        return groupService.getBasicGroup(groupId, member);
    }

    @GetMapping("/{groupId}/detail")
    public GroupDetailResponse getGroupDetail(@PathVariable final Long groupId,
        @AuthMemberId final Long memberId) {
        return groupService.getDetailGroup(groupId, memberId);
    }

    @DeleteMapping("/{groupId}")
    public GroupDeleteResponse delete(@AuthMember final Member member,
        @PathVariable final Long groupId) {
        return groupService.delete(member, groupId);
    }

}
