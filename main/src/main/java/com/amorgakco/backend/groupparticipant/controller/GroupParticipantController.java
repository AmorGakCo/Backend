package com.amorgakco.backend.groupparticipant.controller;

import com.amorgakco.backend.global.argumentresolver.AuthMemberId;
import com.amorgakco.backend.group.dto.LocationVerificationRequest;
import com.amorgakco.backend.groupparticipant.dto.GroupParticipationHistoryResponse;
import com.amorgakco.backend.groupparticipant.dto.LocationVerificationResponse;
import com.amorgakco.backend.groupparticipant.dto.TardinessRequest;
import com.amorgakco.backend.groupparticipant.dto.TemperatureResponse;
import com.amorgakco.backend.groupparticipant.service.GroupParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/group-participants")
@RequiredArgsConstructor
public class GroupParticipantController {

    private final GroupParticipantService groupParticipantService;

    @PatchMapping("/locations")
    public LocationVerificationResponse verifyLocation(
        @AuthMemberId final Long memberId,
        @RequestBody final LocationVerificationRequest request) {
        return groupParticipantService.verifyParticipantLocation(request, memberId);
    }

    @GetMapping("/current-history")
    public GroupParticipationHistoryResponse getCurrentParticipationHistory
        (@RequestParam final Integer page, @AuthMemberId final Long memberId) {
        return groupParticipantService.getCurrentGroupParticipationHistories(memberId, page);
    }

    @GetMapping("/past-history")
    public GroupParticipationHistoryResponse getPastParticipationHistory
        (@RequestParam final Integer page, @AuthMemberId final Long memberId) {
        return groupParticipantService.getPastGroupParticipationHistories(memberId, page);
    }

    @DeleteMapping("/groups/{groupId}")
    public void withdraw(@PathVariable final Long groupId, @AuthMemberId final Long memberId) {
        groupParticipantService.withdraw(groupId, memberId);
    }

    @PostMapping("/groups/{groupId}/tardiness")
    public void tardy(
        @PathVariable final Long groupId,
        @AuthMemberId final Long memberId,
        @RequestBody @Valid final TardinessRequest tardinessRequest) {
        groupParticipantService.tardy(groupId, memberId, tardinessRequest);
    }

    @PatchMapping("/{targetMemberId}/groups/{groupId}/temperature-increase")
    public TemperatureResponse upTemperature(
        @PathVariable final Long targetMemberId,
        @PathVariable final Long groupId,
        @AuthMemberId final Long requestMemberId
    ) {
        return groupParticipantService.increaseTemperature(groupId, requestMemberId,
            targetMemberId);
    }

    @PatchMapping("/{targetMemberId}/groups/{groupId}/temperature-decrease")
    public TemperatureResponse downTemperature(
        @PathVariable final Long targetMemberId,
        @PathVariable final Long groupId,
        @AuthMemberId final Long requestMemberId
    ) {
        return groupParticipantService.decreaseTemperature(groupId, requestMemberId,
            targetMemberId);
    }
}
