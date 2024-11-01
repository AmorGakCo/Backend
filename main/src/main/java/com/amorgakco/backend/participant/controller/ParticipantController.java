package com.amorgakco.backend.participant.controller;

import com.amorgakco.backend.global.argumentresolver.AuthMemberId;
import com.amorgakco.backend.group.dto.LocationVerificationRequest;
import com.amorgakco.backend.participant.dto.ParticipationHistoryPagingResponse;
import com.amorgakco.backend.participant.dto.TardinessRequest;
import com.amorgakco.backend.participant.dto.TemperatureResponse;
import com.amorgakco.backend.participant.service.ParticipantService;
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
@RequestMapping("/participants")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @PatchMapping("/locations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void verifyLocation(
            @AuthMemberId final Long memberId,
            @RequestBody final LocationVerificationRequest request) {
        participantService.verifyParticipantLocation(request, memberId);
    }

    @GetMapping("/current-history")
    public ParticipationHistoryPagingResponse getCurrentParticipationHistory
            (@RequestParam final Integer page, @AuthMemberId final Long memberId) {
        return participantService.getCurrentParticipationHistories(memberId, page);
    }

    @GetMapping("/past-history")
    public ParticipationHistoryPagingResponse getPastParticipationHistory
            (@RequestParam final Integer page, @AuthMemberId final Long memberId) {
        return participantService.getPastParticipationHistories(memberId, page);
    }

    @DeleteMapping("/groups/{groupId}")
    public void withdraw(@PathVariable final Long groupId, @AuthMemberId final Long memberId) {
        participantService.withdraw(groupId, memberId);
    }

    @PostMapping("/groups/{groupId}/tardiness")
    public void tardy(
            @PathVariable final Long groupId,
            @AuthMemberId final Long memberId,
            @RequestBody @Valid final TardinessRequest tardinessRequest) {
        participantService.tardy(groupId, memberId, tardinessRequest);
    }

    @PatchMapping("/{targetMemberId}/groups/{groupId}/temperature-increase")
    public TemperatureResponse upTemperature(
            @PathVariable final Long targetMemberId,
            @PathVariable final Long groupId,
            @AuthMemberId final Long requestMemberId
    ) {
        return participantService.increaseTemperature(groupId, requestMemberId, targetMemberId);
    }

    @PatchMapping("/{targetMemberId}/groups/{groupId}/temperature-decrease")
    public TemperatureResponse downTemperature(
            @PathVariable final Long targetMemberId,
            @PathVariable final Long groupId,
            @AuthMemberId final Long requestMemberId
    ) {
        return participantService.decreaseTemperature(groupId, requestMemberId, targetMemberId);
    }
}
