package com.amorgakco.notification.consumer.slack;

import com.amorgakco.notification.dto.FcmMessageRequest;
import com.amorgakco.notification.dto.SmsMessageRequest;
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

    public void sendFcmMessage(final FcmMessageRequest request) {
        restClient
                .post()
                .uri(slackSecret)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new SlackRequest(request.toString()))
                .retrieve()
                .toBodilessEntity();
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
