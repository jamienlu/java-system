package com.asura.rocketmq.jms;

import com.alibaba.fastjson.JSONObject;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RocketProducer {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    @Value("${rocketmq.topic}")
    private String topic;

    public void produce(String topicName, Object obj) {
        rocketMQTemplate.getProducer().setSendMsgTimeout(5000);
        String topic = StringUtil.isNullOrEmpty(topicName) ? "test" : topicName;
        log.info("准备发送的主题为:{}", topic);
        String obj2String = JSONObject.toJSONString(obj);
        log.info("准备发送消息为:{}", obj2String);
        rocketMQTemplate.convertAndSend(topicName, obj);
    }
}
