package com.amorgakco.notification.handler;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitmqFailHandler {

    private final RabbitTemplate rabbitTemplate;
    private static final int FAIL_THRESHOLD = 2;

    public void handle(final Channel channel, final Message message){
        int failCount = (int) message.getMessageProperties().getHeaders().get("x-retries-count");
        if(failCount < FAIL_THRESHOLD){
            sendNack(channel,message);
        }
    }

    private void sendNack(final Channel channel, final Message message) {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try{
            channel.basicNack(deliveryTag,false,false);
        }catch (IOException e){
            log.error("nack error : {}",message);
        }
    }
}
