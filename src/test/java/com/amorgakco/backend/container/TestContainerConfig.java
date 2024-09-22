package com.amorgakco.backend.container;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.utility.DockerImageName;

public class TestContainerConfig
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis:latest")).withExposedPorts(6379);

    static {
        REDIS_CONTAINER.start();
    }

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {

        TestPropertyValues.of(
                        "spring.data.redis.host=" + REDIS_CONTAINER.getHost(),
                        "spring.data.redis.port=" + REDIS_CONTAINER.getFirstMappedPort())
                .applyTo(applicationContext.getEnvironment());
    }
}