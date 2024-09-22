package com.amorgakco.backend.container;

import com.redis.testcontainers.RedisContainer;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.utility.DockerImageName;

@Configuration
@Profile("test")
public class TestContainerConfig
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String REDISSON_HOST_PREFIX = "redis://";
    private static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis:latest"))
                    .withExposedPorts(6379);

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(REDISSON_HOST_PREFIX + REDIS_CONTAINER.getHost() + ":" + REDIS_CONTAINER.getFirstMappedPort());
        return Redisson.create(config);
    }

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