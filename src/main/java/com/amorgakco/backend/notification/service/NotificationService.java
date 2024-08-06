package com.amorgakco.backend.notification.service;

import com.amorgakco.backend.notification.domain.Notification;
import com.amorgakco.backend.notification.dto.NotificationMessageResponse;
import com.amorgakco.backend.notification.repository.NotificationRepository;
import com.amorgakco.backend.notification.service.mapper.NotificationMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Integer PAGE_SIZE = 10;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationMessageResponse getNotifications(final Long memberId, final Integer page) {
        final PageRequest pageRequest =
                PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));
        final Slice<Notification> notificationSlice =
                notificationRepository.findByReceiver(memberId, pageRequest);
        return notificationMapper.toNotificationMessageResponse(notificationSlice);
    }
}
