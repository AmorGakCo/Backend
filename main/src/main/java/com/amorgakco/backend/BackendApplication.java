package com.amorgakco.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableJpaAuditing
@EnableJpaRepositories(
    basePackages = "com.amorgakco.backend",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {com.amorgakco.backend.notification.repository.NotificationRepository.class,
            com.amorgakco.backend.fcmtoken.repository.FcmTokenRepository.class,
            com.amorgakco.backend.jwt.repository.RefreshTokenRepository.class}
    )
)
public class BackendApplication {

    public static void main(final String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
