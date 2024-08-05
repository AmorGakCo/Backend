package com.amorgakco.backend.groupjoining.controller;

import com.amorgakco.backend.global.argumentresolver.AuthMember;
import com.amorgakco.backend.groupjoining.service.GroupJoiningService;
import com.amorgakco.backend.member.domain.Member;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupJoiningController {

    private final GroupJoiningService groupJoiningService;

    @PostMapping("/{groupId}/joining")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void requestJoin(@PathVariable final Long groupId, @AuthMember final Member member) {
        groupJoiningService.requestJoin(groupId, member);
    }

    @PatchMapping
    public void approveJoin() {}

    @DeleteMapping
    public void rejectJoin() {}
}
