package com.asura.kafka;

import com.asura.kafka.jms.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringKafkaStartApplication {
	@Autowired
	public SpringKafkaStartApplication(KafkaProducer kafkaProducer) {
		kafkaProducer.produce("topic1","hello1 world1!");
		kafkaProducer.produce("","hello world!");
		kafkaProducer.produce("topic2","hello2 world2!");

	}
	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaStartApplication.class, args);
	}

}
