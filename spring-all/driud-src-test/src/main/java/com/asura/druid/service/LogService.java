package com.asura.druid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class LogService implements ILogService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int select() {
        return jdbcTemplate.queryForObject("select count(1) from undo_log", Integer.class);
    }
}
