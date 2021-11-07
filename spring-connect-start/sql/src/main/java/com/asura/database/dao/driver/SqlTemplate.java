package com.asura.database.dao.driver;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Getter
public class SqlTemplate {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    @Qualifier("sessionRedisTemplate")
    private RedisTemplate sessionrRedisTemplate;
    @Autowired
    @Qualifier("cacheRedisTemplate")
    private RedisTemplate cacheRedisTemplate;
}
