package com.grijesh.cricket;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class BaseIntegrationTest {

    @Container
    public static MySQLContainer mySQLContainer = new MySQLContainer<>("mysql:5")
            .withDatabaseName("cricket")
            .withPassword("cricket123");

    @LocalServerPort
    protected Integer randomServerPort;

    @BeforeAll
    public static void beforeAll() {
        System.setProperty("DB_URL", mySQLContainer.getJdbcUrl());
    }

    protected String getApplicationUrl() {
        return String.format("http://localhost:%d", randomServerPort);
    }

}
