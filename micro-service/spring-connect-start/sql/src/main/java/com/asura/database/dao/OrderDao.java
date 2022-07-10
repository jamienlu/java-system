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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Slf4j
public class OrderDao {
    @Autowired
    private MultiRouteDataSource multiDataSource;

    @DataSourceBeanName
    public void insert() throws SQLException {
        String sql = "insert into asura_category(name,parent_id,is_parent,sort) values('家用电器','-1',true,0)";
        PreparedStatement preparedStatement = multiDataSource.getConnection().prepareStatement(sql);
        int resrult = preparedStatement.executeUpdate();
    }

    @DataSourceBeanName("slave1")
    public void read1() throws SQLException {
        String sql = "select count(1) from asura_category";
        int result = new JdbcTemplate(multiDataSource).queryForObject(sql, Integer.class);
        log.info("read1:" + result);
    }

    @DataSourceBeanName("slave2")
    public void read2() throws SQLException {
        String sql = "select count(1) from asura_category";
        int result = new JdbcTemplate(multiDataSource).queryForObject(sql, Integer.class);
        log.info("read2:" + result);
    }
}
