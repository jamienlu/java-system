package com.asura.shradingspheretest.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class JdbcTemplateConf {
    @Bean
    public JdbcTemplate createJdbcTemplate(final DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
