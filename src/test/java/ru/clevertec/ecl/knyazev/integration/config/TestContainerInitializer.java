package ru.clevertec.ecl.knyazev.integration.config;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public class TestContainerInitializer {
    private static final String POSTGRESQL_CONTAINER_VERSION = "postgres:16.1";

    private static final String DATASOURCE_URL = "spring.datasource.url";

    private static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer(POSTGRESQL_CONTAINER_VERSION);


    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add(DATASOURCE_URL, postgreSQLContainer::getJdbcUrl);
    }

    @BeforeAll
    static void startUp() {
        postgreSQLContainer.start();
    }
}
