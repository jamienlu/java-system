package com.asura.database.conf.datasource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class Slave1Config {
    @Value("${datasource.slave1.url}")
    private String url;
    @Value("${datasource.slave1.username}")
    private String username;
    @Value("${datasource.slave1.password}")
    private String password;
    @Value("${datasource.slave1.driverClassName}")
    private String driverClassName;
}
