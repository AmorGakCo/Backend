package com.amorgakco.backend.groupapplication.controller;

import com.amorgakco.backend.global.argumentresolver.AuthMember;
import com.amorgakco.backend.global.argumentresolver.AuthMemberId;
import com.amorgakco.backend.groupapplication.dto.ApplicationRegisterResponse;
import com.amorgakco.backend.groupapplication.dto.ApproveResponse;
import com.amorgakco.backend.groupapplication.service.GroupApplicationService;
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
@RequestMapping("/api/groups")
public class GroupApplicationController {

    private final GroupApplicationService groupApplicationService;

    @PostMapping("/{groupId}/applications")
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationRegisterResponse apply(@PathVariable final Long groupId,
        @AuthMemberId final Long memberId) {
        return groupApplicationService.apply(groupId, memberId);
    }

    @PostMapping("/{groupId}/applications/{memberId}")
    public ApproveResponse approve(
        @PathVariable final Long groupId,
        @PathVariable final Long memberId,
        @AuthMember final Member member) {
        return groupApplicationService.approve(groupId, memberId, member);
    }

    @PatchMapping("/{groupId}/applications/{memberId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reject(
        @PathVariable final Long groupId,
        @PathVariable final Long memberId,
        @AuthMemberId final Long hostId) {
        groupApplicationService.reject(groupId, memberId, hostId);
    }

}
