package com.asura.database.dao;

import com.alibaba.fastjson.JSONObject;
import com.asura.database.conf.datasource.DataSourceBeanName;
import com.asura.database.conf.datasource.MultiRouteDataSource;
import com.asura.database.dao.driver.SqlTemplate;
import jdk.nashorn.internal.scripts.JD;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderDao {
    @Autowired
    private MultiRouteDataSource dataSource;

    @DataSourceBeanName
    public void insert() {
        String sql = "insert into asura_category(name,parent_id,is_parent,sort) values('家用电器','-1',true,0)";
        new JdbcTemplate(dataSource).update(sql);
    }

    @DataSourceBeanName("slave1")
    public void read1() {
        String sql = "select count(1) from asura_category";
        log.info("read1:" + new JdbcTemplate(dataSource).queryForList(sql));
    }

    @DataSourceBeanName("slave2")
    public void read2() {
        String sql = "select count(1) from asura_category";
        log.info("read2:" + new JdbcTemplate(dataSource).queryForObject(sql, Integer.class));
    }
}
