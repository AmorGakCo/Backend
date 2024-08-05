package com.amorgakco.backend.global.config;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoolSmsConfig {

    @Bean
    public DefaultMessageService defaultMessageService(
            @Value("${api-key}") final String apiKey,
            @Value("${api-secret}") final String apiSecret) {
        return NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }
}
