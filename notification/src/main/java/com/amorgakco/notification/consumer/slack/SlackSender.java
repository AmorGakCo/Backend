package com.amorgakco.notification.consumer.slack;

import com.amorgakco.notification.dto.SmsMessageRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class SlackSender {

    private static final String ALERT_MESSAGE_TEMPLATE = "Origin Message Info %s , Retry Count %d";
    private final RestClient restClient;
    private final String slackSecret;

    public SlackSender(
            @Value("${slack-url}") final String slackUrl,
            @Value("${slack-secret}") final String slackSecret) {
        this.restClient = RestClient.create(slackUrl);
        this.slackSecret = slackSecret;
    }

    public void sendFailedMessage(final String failedMessageInfo, int retryCount) {
        final String alertMessage = createAlertMessage(failedMessageInfo, retryCount);
        restClient
                .post()
                .uri(slackSecret)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new SlackRequest(alertMessage))
                .retrieve()
                .toBodilessEntity();
    }

    private String createAlertMessage(final String failedMessageInfo, int retryCount) {
        return ALERT_MESSAGE_TEMPLATE.formatted(failedMessageInfo, retryCount);
    }

    public void sendSmsMessage(final SmsMessageRequest request) {
        restClient
                .post()
                .uri(slackSecret)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new SlackRequest(request.toString()))
                .retrieve()
                .toBodilessEntity();
    }
}
