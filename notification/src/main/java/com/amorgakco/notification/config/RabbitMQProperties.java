package com.amorgakco.notification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.rabbitmq")
public record RabbitMQProperties(String host, Integer port, String username, String password) {

}
