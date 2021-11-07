package com.asura.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;


@SpringBootApplication
@EnableAutoConfiguration
public class SpringConnectStartApplication {
	@Autowired
	public SpringConnectStartApplication(@Qualifier("sessionRedisTemplate")RedisTemplate redisTemplate, MongoTemplate mongoTemplate) {
		ValueOperations valueOperations = redisTemplate.opsForValue();
		valueOperations.set("name","asura", 10000, TimeUnit.SECONDS);
		System.out.println("reactiveRedisTemplate success:" + valueOperations.get("name"));
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringConnectStartApplication.class, args);
	}

}
