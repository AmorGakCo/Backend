package com.amorgakco.backend.container;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = {TestContainerConfig.class})
public @interface IntegrationTest {
}
