package com.grijesh.cricket.config;

import com.zaxxer.hikari.HikariConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DataSourceConfig.class)
@EnableConfigurationProperties
public class DataSourceConfigTest {

    @Autowired
    private HikariConfig hikariConfig;

    @MockBean
    private DataSource dataSource;

    @Test
    @DisplayName("Should Able to create hikari config from properties in yml file")
    public void shouldBeAbleToCreateHikariConfigWithOverriddenProperties() {
        assertThat(hikariConfig.getConnectionTimeout()).isEqualTo(30000L);
        assertThat(hikariConfig.getMaximumPoolSize()).isEqualTo(5);
        assertThat(hikariConfig.getDriverClassName()).isEqualTo("com.mysql.cj.jdbc.Driver");
        assertThat(hikariConfig.getConnectionTestQuery()).isEqualTo("SELECT 1");
        assertThat(hikariConfig.getJdbcUrl()).isEqualTo("jdbc:mysql://localhost:3306/cricket");
    }

}
