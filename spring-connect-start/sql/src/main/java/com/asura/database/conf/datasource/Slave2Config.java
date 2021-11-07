package com.asura.database.conf.datasource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class Slave2Config {
    @Value("${datasource.slave2.url}")
    private String url;
    @Value("${datasource.slave2.username}")
    private String username;
    @Value("${datasource.slave2.password}")
    private String password;
    @Value("${datasource.slave2.driverClassName}")
    private String driverClassName;
}
