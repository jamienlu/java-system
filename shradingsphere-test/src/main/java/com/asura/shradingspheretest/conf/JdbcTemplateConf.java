package com.asura.shradingspheretest.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
public class JdbcTemplateConf {
    @Resource
    private DataSource dataSource;

    @Bean
    public JdbcTemplate createJdbcTemplate() {
        return new JdbcTemplate(dataSource);
    }
}
