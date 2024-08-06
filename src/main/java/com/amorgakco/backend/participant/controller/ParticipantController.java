package com.amorgakco.backend.participant.controller;

import com.amorgakco.backend.global.argumentresolver.AuthMember;
import com.amorgakco.backend.global.argumentresolver.AuthMemberId;
import com.amorgakco.backend.group.dto.LocationVerificationRequest;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.participant.dto.ParticipationHistoryResponse;
import com.amorgakco.backend.participant.service.ParticipantService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @GetMapping("/history")
    public ParticipationHistoryResponse getParticipationHistory(
            @RequestParam final Integer page, @AuthMember final Member member) {
        return participantService.getParticipationHistory(member, page);
    }
}
