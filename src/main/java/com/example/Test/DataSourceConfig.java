package com.example.Test;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
    
//    @Value("${spring.datasource.url}")
//    private String url;
//    
//    @Value("${spring.datasource.username}")
//    private String username;
//    
//    @Value("${spring.datasource.password}")
//    private String password;
//    
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl(url);
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        return dataSource;
//    }
    
    // Další konfigurační beany pro JdbcTemplate, EntityManagerFactory, atd.
}

