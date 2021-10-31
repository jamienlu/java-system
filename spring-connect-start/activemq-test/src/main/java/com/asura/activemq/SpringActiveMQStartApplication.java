package com.asura.activemq;

import com.asura.activemq.dao.MessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class SpringActiveMQStartApplication {
	@Autowired
	public SpringActiveMQStartApplication(MessageTemplate messageTemplate) {
		messageTemplate.produceMsg("hello world");
		messageTemplate.produceTopicMsg("topic hello world");
	} ;

	public static void main(String[] args) {
		SpringApplication.run(SpringActiveMQStartApplication.class, args);
	}

}
