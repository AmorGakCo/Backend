package com.amorgakco.backend.groupapplication.controller;

import com.amorgakco.backend.global.argumentresolver.AuthMember;
import com.amorgakco.backend.global.argumentresolver.AuthMemberId;
import com.amorgakco.backend.groupapplication.dto.ApplicationRegisterResponse;
import com.amorgakco.backend.groupapplication.dto.ApproveResponse;
import com.amorgakco.backend.groupapplication.dto.RejectResponse;
import com.amorgakco.backend.groupapplication.service.GroupApplicationService;
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
import retrofit2.http.Path;

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

    @PostMapping("/{groupId}/applications/{memberId}/notifications/{notificationId}")
    public ApproveResponse approve(
        @PathVariable final Long groupId,
        @PathVariable final Long memberId,
        @AuthMember final Member hostId,
        @PathVariable final String notificationId) {
        return groupApplicationService.approve(groupId, memberId, hostId,notificationId);
    }

    @PatchMapping("/{groupId}/applications/{memberId}/notifications/{notificationId}")
    public RejectResponse reject(
        @PathVariable final Long groupId,
        @PathVariable final Long memberId,
        @AuthMemberId final Long hostId,
        @PathVariable final String notificationId) {
        return groupApplicationService.reject(groupId, memberId, hostId,notificationId);
    }

}
