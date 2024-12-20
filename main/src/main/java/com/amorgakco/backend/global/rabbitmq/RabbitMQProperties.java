package com.amorgakco.backend.global.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.rabbitmq")
public record RabbitMQProperties(String host, Integer port, String username, String password) {

}
