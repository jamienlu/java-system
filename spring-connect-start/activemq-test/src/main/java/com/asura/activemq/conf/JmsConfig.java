package com.asura.activemq.conf;

import lombok.Getter;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import javax.jms.Topic;

@Component
@Getter
public class JmsConfig {
    @Value("${queue.activemq}")
    private String myQueue;
    @Value("${queue.topicName}")
    private String topicName;
    @Value("${queue.fixedDelay}")
    private Long fixedDelay;
    @Bean
    public Queue queue(){
        return new ActiveMQQueue(myQueue);
    }
    @Bean
    public Topic topic(){
        return new ActiveMQTopic(topicName);
    }
}
