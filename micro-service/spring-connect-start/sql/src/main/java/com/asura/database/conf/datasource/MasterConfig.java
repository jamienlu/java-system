package com.asura.database.conf.datasource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class MasterConfig {
    @Value("${datasource.master.url}")
    private String url;
    @Value("${datasource.master.username}")
    private String username;
    @Value("${datasource.master.password}")
    private String password;
    @Value("${datasource.master.driverClassName}")
    private String driverClassName;
}
