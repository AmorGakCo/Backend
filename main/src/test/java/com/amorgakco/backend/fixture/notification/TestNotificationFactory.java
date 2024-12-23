package com.amorgakco.backend.fixture.notification;

import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.notification.dto.NotificationMessage;
import com.amorgakco.backend.notification.dto.NotificationMessageResponse;
import com.amorgakco.backend.notification.dto.NotificationRequest;
import com.amorgakco.backend.notification.service.NotificationCreator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
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
        final Group group = TestGroupFactory.createActiveGroup(receiver);
        final NotificationRequest request =
            NotificationCreator.participationRequest(sender, receiver, group);
        return NotificationMessage.builder()
            .content(request.content())
            .title(request.title())
            .notificationType(request.notificationType())
            .groupId(group.getId())
            .receiverMemberId(receiver.getId())
            .senderMemberId(sender.getId())
            .build();
    }
}
