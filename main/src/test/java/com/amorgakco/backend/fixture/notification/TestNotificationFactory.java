package com.amorgakco.backend.fixture.notification;

import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.notification.dto.NotificationMessage;
import com.amorgakco.backend.notification.dto.NotificationMessageResponse;
import com.amorgakco.backend.notification.infrastructure.consumer.NotificationRequest;
import com.amorgakco.backend.notification.service.NotificationCreator;
import java.util.List;

public class TestNotificationFactory {

    public static NotificationMessageResponse notificationMessageResponse() {
        final List<NotificationMessage> notificationMessages =
            List.of(notificationMessage(), notificationMessage(), notificationMessage());
        return NotificationMessageResponse.builder()
            .page(0)
            .hasNext(false)
            .notificationMessages(notificationMessages)
            .elementSize(notificationMessages.size())
            .build();
    }

    private static NotificationMessage notificationMessage() {
        final Member sender = TestMemberFactory.create(1L);
        final Member receiver = TestMemberFactory.create(2L);
        final NotificationRequest request =
            NotificationCreator.participationRequest(sender, receiver, "mogakco");
        return NotificationMessage.builder()
            .content(request.content())
            .title(request.title())
            .build();
    }
}
