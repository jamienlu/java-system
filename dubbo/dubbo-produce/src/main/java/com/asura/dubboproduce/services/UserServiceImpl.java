package com.asura.dubboproduce.services;

import com.asura.dubboproduce.dao.UserDao;
import com.asura.exchangemoney.api.UserAccount;
import com.asura.exchangemoney.api.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.annotation.HmilyTCC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@DubboService(version = "1.0.0")
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    public UserDao userDao;
    @Autowired
    @Qualifier("sessionRedisTemplate")
    public RedisTemplate redisTemplate;

    @Override
    @Transactional
    @HmilyTCC(confirmMethod = "confirm", cancelMethod = "cancel")
    public List<UserAccount> exchangeCurrency(String uuid, int userId, int srcType, int targetType, int num) {
        log.info("start exchangeCurrency");
        boolean isSuccess = redisTemplate.opsForValue().setIfAbsent(uuid, false, Duration.ofSeconds(3));
        log.info("check valid:" + isSuccess);
        if (isSuccess) {
            if (userDao.checkUserAccountNum(userId,srcType) < num) {
                log.error("exchangeCurrency user srcType not ");
            } else {
                userDao.insertUserAccount(userId,srcType,-num);
                userDao.insertUserAccount(userId,targetType,userDao.selectRate(targetType) * num);
                redisTemplate.opsForValue().setIfPresent(uuid, true, Duration.ofSeconds(3));
                log.info("try UserAccount success!");
            }
            return userDao.queryUserAccounts(userId);
        }
        return new ArrayList<>();
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean confirm(String uuid, int userId, int srcType, int targetType, int num) {
        if(redisTemplate.opsForValue().get(uuid) != null) {
            log.info("confirm UserAccount success!");
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean cancel(String uuid, int userId, int srcType, int targetType, int num) {
        if((boolean)redisTemplate.opsForValue().get(uuid)) {
            log.info("cancel UserAccount success!");
            userDao.insertUserAccount(userId,srcType,num);
            userDao.insertUserAccount(userId,targetType,userDao.selectRate(targetType) * -num);
            redisTemplate.opsForValue().decrement(uuid, 1000);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
