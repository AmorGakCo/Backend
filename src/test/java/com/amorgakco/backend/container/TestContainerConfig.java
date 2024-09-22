package com.amorgakco.backend.container;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;


public class TestContainerConfig
        implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final RedisContainer REDIS_CONTAINER =
            new RedisContainer(DockerImageName.parse("redis:latest")).withExposedPorts(6379);
    private static final MySQLContainer MYSQL_CONTAINER =
            new MySQLContainer(DockerImageName.parse("mysql:8.0.22"))
                    .withDatabaseName("amorgakco")
                    .withUsername("admin")
                    .withPassword("1234");

    static {
        REDIS_CONTAINER.start();
        MYSQL_CONTAINER.start();
    }

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        final String jdbcUrl = MYSQL_CONTAINER.getJdbcUrl();
        final String username = MYSQL_CONTAINER.getUsername();
        final String password = MYSQL_CONTAINER.getPassword();
        TestPropertyValues.of(
                        "spring.data.redis.host=" + REDIS_CONTAINER.getHost(),
                        "spring.data.redis.port=" + REDIS_CONTAINER.getFirstMappedPort(),
                        "spring.datasource.url=" + jdbcUrl,
                        "spring.datasource.username=" + username,
                        "spring.datasource.password=" + password)
                .applyTo(applicationContext.getEnvironment());
    }
}