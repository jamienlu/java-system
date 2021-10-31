package com.asura.activemq.dao;

import com.asura.activemq.conf.JmsConfig;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;

@Component
@Getter
public class MessageTemplate {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Topic topic;
    @Autowired
    private Queue queue;
    private static final class MessageTemplateBuider {
        private static final MessageTemplate instance = new MessageTemplate();
    }
    private MessageTemplate() {
    }
    public static MessageTemplate getInstance() {
        return MessageTemplateBuider.instance;
    }

    public void produceMsg(Object msg){
        jmsMessagingTemplate.convertAndSend(queue, msg);
    }

    public void produceTopicMsg(Object msg){
        jmsMessagingTemplate.convertAndSend(topic, msg);
    }

    @JmsListener(destination = "${queue.activemq}")
    public void received(TextMessage textMessage) throws JMSException {
        System.out.println("********消费者收到消息：" + textMessage.getText());
    }
}
