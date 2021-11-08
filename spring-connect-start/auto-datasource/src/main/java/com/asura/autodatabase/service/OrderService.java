package com.asura.autodatabase.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void writeRead() {
        String sql1 = "insert into asura_category(name,parent_id,is_parent,sort) values('家用电器','-1',true,0)";
        int result1 = jdbcTemplate.update(sql1);
        String sql2 = "select count(1) from asura_category";
        int result2 = jdbcTemplate.queryForObject(sql2, Integer.class);
        log.info("write:" + result1 + "____read:" + result2);
    }

    public void read1() {
        String sql = "select count(1) from asura_category";
        int result = jdbcTemplate.queryForObject(sql, Integer.class);
        log.info("read1:" + result);
    }

    public void read2() {
        String sql = "select count(1) from asura_category";
        int result = jdbcTemplate.queryForObject(sql, Integer.class);
        log.info("read2:" + result);
    }

    public void read3() {
        String sql = "select count(1) from asura_category";
        int result = jdbcTemplate.queryForObject(sql, Integer.class);
        log.info("read3:" + result);
    }

}
