package com.asura.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class SpringKafkaStartApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaStartApplication.class, args);
	}

}
