package com.asura.database.conf.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {
    @Autowired
    private MasterConfig masterConfig;

    @Autowired
    private Slave1Config slave1Config;

    @Autowired
    private Slave2Config slave2Config;

    public DataSource masterDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(masterConfig.getUrl());
        dataSource.setUsername(masterConfig.getUsername());
        dataSource.setPassword(masterConfig.getPassword());
        dataSource.setDriverClassName(masterConfig.getDriverClassName());
        return dataSource;
    }

    public DataSource slave1DataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(slave1Config.getUrl());
        dataSource.setUsername(slave1Config.getUsername());
        dataSource.setPassword(slave1Config.getPassword());
        dataSource.setDriverClassName(slave1Config.getDriverClassName());
        return dataSource;
    }

    public DataSource slave2DataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(slave2Config.getUrl());
        dataSource.setUsername(slave2Config.getUsername());
        dataSource.setPassword(slave2Config.getPassword());
        dataSource.setDriverClassName(slave2Config.getDriverClassName());
        return dataSource;
    }

    @Bean(name = "multiDataSource")
    public MultiRouteDataSource exampleRouteDataSource() {
        MultiRouteDataSource multiDataSource = new MultiRouteDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("master", masterDataSource());
        targetDataSources.put("slave1", slave1DataSource());
        targetDataSources.put("slave2", slave2DataSource());
        multiDataSource.setTargetDataSources(targetDataSources);
        multiDataSource.setDefaultTargetDataSource(masterDataSource());
        return multiDataSource;
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager() {
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(exampleRouteDataSource());
        return manager;
    }
}
