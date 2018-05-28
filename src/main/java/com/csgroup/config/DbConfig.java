package com.csgroup.config;

import org.hsqldb.jdbc.JDBCDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Bean
    public DataSource dataSource() {

        JDBCDataSource ds = new JDBCDataSource();
        ds.setDatabaseName("MnitoringAlerts");
        ds.setUser("SA");
        ds.setPassword("");
        ds.setUrl(jdbcUrl);
        return ds;
    }
}
