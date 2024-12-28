package com.amorgakco.backend.global.config;

import com.amorgakco.backend.global.rabbitmq.ExchangeName;
import com.amorgakco.backend.global.rabbitmq.QueueName;
import com.amorgakco.backend.global.rabbitmq.RabbitMQProperties;
import com.amorgakco.backend.global.rabbitmq.RoutingKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@EnableRabbit
@Configuration
@RequiredArgsConstructor
@Profile("!test")
public class RabbitMQConfig {

    private final RabbitMQProperties properties;
    private static final int SMS_DELAY_TTL = 1000;
    private static final int FCM_DELAY_TTL = 1000;

    @Bean
    public RabbitAdmin rabbitAdmin(final ConnectionFactory connectionFactory) {
        final RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.declareQueue(fcmQueue());
        rabbitAdmin.declareQueue(smsQueue());
        rabbitAdmin.declareQueue(fcmDelayQueue());
        rabbitAdmin.declareQueue(smsDelayQueue());
        rabbitAdmin.declareQueue(fcmDeadLetterQueue());
        rabbitAdmin.declareQueue(smsDeadLetterQueue());
        rabbitAdmin.declareExchange(notificationExchange());
        rabbitAdmin.declareExchange(delayExchange());
        rabbitAdmin.declareExchange(deadLetterExchange());
        rabbitAdmin.declareBinding(bindingFcmQueue());
        rabbitAdmin.declareBinding(bindingSmsQueue());
        rabbitAdmin.declareBinding(bindingFcmDeadLetterQueue());
        rabbitAdmin.declareBinding(bindingSmsDeadLetterQueue());
        rabbitAdmin.declareBinding(bindingFcmDelayQueue());
        rabbitAdmin.declareBinding(bindingSmsDelayQueue());
        return rabbitAdmin;
    }

    @Bean
    public Queue fcmQueue() {
        return QueueBuilder.durable(QueueName.FCM_QUEUE.getName())
            .deadLetterRoutingKey(RoutingKey.NOTIFICATION_FCM_DEAD_LETTER.getKey())
            .deadLetterExchange(ExchangeName.NOTIFICATION_DEAD_LETTER.getName())
            .build();
    }

    @Bean
    public Queue smsQueue() {
        return QueueBuilder.durable(QueueName.SMS_QUEUE.getName())
            .deadLetterRoutingKey(RoutingKey.NOTIFICATION_SMS_DEAD_LETTER.getKey())
            .deadLetterExchange(ExchangeName.NOTIFICATION_DEAD_LETTER.getName())
            .build();
    }

    @Bean
    public Queue smsDelayQueue(){
        return QueueBuilder.durable(QueueName.SMS_DELAY_QUEUE.getName())
            .deadLetterExchange(ExchangeName.NOTIFICATION.getName())
            .deadLetterRoutingKey(RoutingKey.NOTIFICATION_SMS.getKey())
            .ttl(SMS_DELAY_TTL)
            .build();
    }

    @Bean
    public Queue fcmDelayQueue(){
        return QueueBuilder.durable(QueueName.FCM_DELAY_QUEUE.getName())
            .deadLetterExchange(ExchangeName.NOTIFICATION.getName())
            .deadLetterRoutingKey(RoutingKey.NOTIFICATION_FCM.getKey())
            .ttl(FCM_DELAY_TTL)
            .build();
    }

    @Bean
    public Queue smsDeadLetterQueue(){
        return new Queue(QueueName.SMS_DEAD_LETTER_QUEUE.getName(),true);
    }

    @Bean
    public Queue fcmDeadLetterQueue(){
        return new Queue(QueueName.FCM_DEAD_LETTER_QUEUE.getName(),true);
    }

    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(ExchangeName.NOTIFICATION.getName());
    }

    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(ExchangeName.NOTIFICATION_DELAY.getName());
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(ExchangeName.NOTIFICATION_DEAD_LETTER.getName());
    }

    @Bean
    public Binding bindingFcmQueue() {
        return BindingBuilder.bind(fcmQueue())
            .to(notificationExchange())
            .with(RoutingKey.NOTIFICATION_FCM.getKey());
    }

    @Bean
    public Binding bindingSmsQueue() {
        return BindingBuilder.bind(smsQueue())
            .to(notificationExchange())
            .with(RoutingKey.NOTIFICATION_SMS.getKey());
    }

    @Bean
    public Binding bindingFcmDelayQueue() {
        return BindingBuilder.bind(fcmDelayQueue())
            .to(delayExchange())
            .with(RoutingKey.NOTIFICATION_FCM_DELAY.getKey());
    }

    @Bean
    public Binding bindingSmsDelayQueue() {
        return BindingBuilder.bind(smsDelayQueue())
            .to(delayExchange())
            .with(RoutingKey.NOTIFICATION_SMS_DELAY.getKey());
    }

    @Bean
    public Binding bindingSmsDeadLetterQueue() {
        return BindingBuilder.bind(smsDeadLetterQueue())
            .to(deadLetterExchange())
            .with(RoutingKey.NOTIFICATION_SMS_DEAD_LETTER.getKey());
    }

    @Bean
    public Binding bindingFcmDeadLetterQueue() {
        return BindingBuilder.bind(fcmDeadLetterQueue())
            .to(deadLetterExchange())
            .with(RoutingKey.NOTIFICATION_FCM_DEAD_LETTER.getKey());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
        final ConnectionFactory connectionFactory, final MessageConverter messageConverter) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        final CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(properties.host());
        connectionFactory.setPort(properties.port());
        connectionFactory.setUsername(properties.username());
        connectionFactory.setPassword(properties.password());
        return connectionFactory;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
