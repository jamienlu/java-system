package com.asura.activemq.conf;

import lombok.Getter;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;
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


    /**
     * JmsListener注解默认只接收queue消息,如果要接收topic消息,需要设置containerFactory
     */
    @Bean
    public JmsListenerContainerFactory<?> topicListenerContainer(ConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory topicListenerContainer = new DefaultJmsListenerContainerFactory();
        topicListenerContainer.setPubSubDomain(true);
        topicListenerContainer.setConnectionFactory(activeMQConnectionFactory);
        return topicListenerContainer;
    }
}
