package com.asura.cache.service;

import com.alibaba.fastjson.JSONArray;
import com.asura.cache.entity.MessageReceive;
import com.asura.cache.entity.Role;
import com.asura.cache.mapper.RoleDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService{
    @Autowired
    RoleDao roleDao;
    @Autowired
    CacheManager cacheManager;
    @Autowired
    @Qualifier("redisTemplate")
    RedisTemplate redisTemplate;
    @Autowired
    @Qualifier("numberRedisTemplate")
    RedisTemplate numberRedisTemplate;
    @Value("${other.totals}")
    Long totals;
    @Override
    public void insert(Role role) {
        boolean ret = redisTemplate.opsForValue().setIfAbsent(role.getUsername(), role, Duration.ofSeconds(3));
        // 分布式锁
        if (ret) {
            log.info("get lock insert role to mysql");
            roleDao.save(role);
            log.info("remove lock :" + redisTemplate.delete(role.getUsername()));
        }
        log.info("end lock");
    }

    @Override
    public List<Role> selectAll() {
        List<Role> roles = new ArrayList<>();
        Object role = redisTemplate.opsForValue().get("roles");
        if (role != null) {
            log.info("redis has roles");
            roles.addAll(JSONArray.parseArray((String) role, Role.class));
        } else {
            List<Role> sqlRoles = roleDao.selectAll();
            roles.addAll(sqlRoles);
            redisTemplate.opsForValue().set("roles", JSONArray.toJSONString(sqlRoles), Duration.ofSeconds(30));
            log.info("save redis");
        }
        return roles;
    }

    @Override
    public void consumer() {
      long currentValue = numberRedisTemplate.opsForValue().increment("totals");
        log.info("current count:" + currentValue);
        if (currentValue > totals) {
            MessageReceive messageReceive = new MessageReceive();
            messageReceive.setCode(Long.valueOf(currentValue).intValue());
            messageReceive.setMessage("库存不足");
            redisTemplate.convertAndSend("pattern", messageReceive);
        } else {
            log.info("库存充足" + currentValue);
            numberRedisTemplate.expire("totals",Duration.ofSeconds(30));
        }
    }
}
