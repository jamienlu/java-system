package com.asura.rocketmq;

import com.asura.rocketmq.jms.RocketProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class SpringRocketStartApplication {
	@Autowired
	public SpringRocketStartApplication(RocketProducer rocketProducer) {
		rocketProducer.produce("topic_test", "hello world!");
	}
	public static void main(String[] args) {
		SpringApplication.run(SpringRocketStartApplication.class, args);
	}

}
