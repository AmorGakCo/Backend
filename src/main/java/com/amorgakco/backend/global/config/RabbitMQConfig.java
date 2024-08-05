package com.amorgakco.backend.global.config;

import com.amorgakco.backend.global.properties.RabbitMQProperties;
import com.amorgakco.backend.global.rabbitmq.ExchangeName;
import com.amorgakco.backend.global.rabbitmq.QueueName;
import com.amorgakco.backend.global.rabbitmq.RoutingKey;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final RabbitMQProperties properties;

    @Bean
    public Queue fcmQueue() {
        return new Queue(QueueName.FCM.getName(), true);
    }

    @Bean
    public Queue smsQueue() {
        return new Queue(QueueName.SMS.getName(), true);
    }

    @Bean
    public Binding bindingSmsQueue(final DirectExchange exchange, final Queue smsQueue) {
        return BindingBuilder.bind(smsQueue)
                .to(exchange)
                .with(RoutingKey.NOTIFICATION_SMS.getKey());
    }

    @Bean
    public Binding bindingFcmQueue(final DirectExchange exchange, final Queue fcmQueue) {
        return BindingBuilder.bind(fcmQueue)
                .to(exchange)
                .with(RoutingKey.NOTIFICATION_FCM.getKey());
    }

    @Bean
    public DirectExchange notificationExchange() {
        return new DirectExchange(ExchangeName.NOTIFICATION.getName());
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
