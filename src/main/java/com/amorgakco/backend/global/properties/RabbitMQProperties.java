package com.amorgakco.backend.global.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.rabbitmq")
public record RabbitMQProperties(String host, String port, String username, String password) {}
