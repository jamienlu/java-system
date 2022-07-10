package com.asura.cache.entity;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Data
public class MessageReceive {
    private int code;
    private String message;

    // 订阅消息
    public void getMessage(String object) {
        String messageReceive = new StringRedisSerializer().deserialize(object.getBytes());
        log.info("receieve message:" + JSONObject.parseObject(messageReceive,MessageReceive.class));
    }
}
