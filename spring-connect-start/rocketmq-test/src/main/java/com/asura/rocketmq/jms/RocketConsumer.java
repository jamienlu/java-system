package com.asura.rocketmq.jms;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RocketMQMessageListener(topic = "${rocketmq.topic}", consumerGroup = "${rocketmq.consumer.group}")
public class RocketConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String msg) {
        log.info("收到消息" + msg);
    }

}
