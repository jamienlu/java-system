package com.asura.database.dao.driver;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Getter
public class NoSqlTemplate {
    private static final class NoSqlTemplateBuider {
        private static final NoSqlTemplate instance = new NoSqlTemplate();
    }
    private NoSqlTemplate() {

    }

    public static NoSqlTemplate getInstance() {
       return NoSqlTemplateBuider.instance;
    }
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    @Qualifier("sessionRedisTemplate")
    private RedisTemplate sessionTemplate;
    @Autowired
    @Qualifier("cacheRedisTemplate")
    private RedisTemplate cacheTemplate;
}
