package com.amorgakco.backend.fcmtoken.controller;

import com.amorgakco.backend.fcmtoken.service.FcmTokenService;
import com.amorgakco.backend.global.argumentresolver.AuthMemberId;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm-tokens")
public class FcmTokenController {

    private final FcmTokenService fcmTokenService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveToken(
            @RequestBody final String notificationToken, @AuthMemberId final Long memberId) {
        fcmTokenService.save(notificationToken, memberId);
    }
}