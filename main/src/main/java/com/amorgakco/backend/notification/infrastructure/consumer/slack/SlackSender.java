package com.amorgakco.backend.notification.infrastructure.consumer.slack;

import com.amorgakco.backend.notification.infrastructure.consumer.NotificationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class SlackSender {

    private final RestClient restClient;
    private final String slackSecret;

    public SlackSender(
        @Value("${slack-url}") final String slackUrl,
        @Value("${slack-secret}") final String slackSecret) {
        this.restClient = RestClient.create(slackUrl);
        this.slackSecret = slackSecret;
    }

    public void sendNotification(final NotificationRequest request) {
        restClient
            .post()
            .uri(slackSecret)
            .contentType(MediaType.APPLICATION_JSON)
            .body(new SlackRequest(request.toString()))
            .retrieve()
            .toBodilessEntity();
    }
}
