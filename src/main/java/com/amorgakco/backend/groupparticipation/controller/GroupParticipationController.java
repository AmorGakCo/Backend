package com.amorgakco.backend.groupparticipation.controller;

import com.amorgakco.backend.global.argumentresolver.AuthMember;
import com.amorgakco.backend.groupparticipation.service.GroupParticipationService;
import com.amorgakco.backend.member.domain.Member;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupParticipationController {

    private final GroupParticipationService groupParticipationService;

    @PostMapping("/{groupId}/participation")
    @ResponseStatus(HttpStatus.CREATED)
    public void participate(@PathVariable final Long groupId, @AuthMember final Member member) {
        groupParticipationService.participate(groupId, member);
    }

    @PostMapping("/{groupId}/participation/{memberId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void approve(
            @PathVariable final Long groupId,
            @PathVariable final Long memberId,
            @AuthMember final Member host) {
        groupParticipationService.approve(groupId, memberId, host);
    }

    @PatchMapping("/{groupId}/participation/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reject(
            @PathVariable final Long groupId,
            @PathVariable final Long memberId,
            @AuthMember final Member host) {
        groupParticipationService.reject(groupId, memberId, host);
    }
}
