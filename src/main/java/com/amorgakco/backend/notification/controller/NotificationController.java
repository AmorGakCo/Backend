package com.amorgakco.backend.notification.controller;

import com.amorgakco.backend.global.argumentresolver.AuthMemberId;
import com.amorgakco.backend.notification.dto.NotificationMessageResponse;
import com.amorgakco.backend.notification.service.NotificationService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public NotificationMessageResponse getNotification(
            @RequestParam final Integer page, @AuthMemberId final Long memberId) {
        return notificationService.getNotifications(memberId, page);
    }
}
