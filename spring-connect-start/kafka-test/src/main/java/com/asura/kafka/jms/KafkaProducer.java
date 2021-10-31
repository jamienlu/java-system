package com.asura.kafka.jms;

import com.alibaba.fastjson.JSONObject;
import com.asura.kafka.conf.KafkaConfig;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Map;

@Component
@Slf4j
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    public Map<String,String> kafkaTopics;

    public void produce(String topicName, Object obj) {
        String topic = StringUtil.isNullOrEmpty(topicName) || StringUtil.isNullOrEmpty(kafkaTopics.get(topicName))
            ? KafkaConfig.TOPIC_TEST : kafkaTopics.get(topicName);
        log.info("准备发送的主题为:{}", topic);
        String obj2String = JSONObject.toJSONString(obj);
        log.info("准备发送消息为:{}", obj2String);
        //发送消息
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, obj);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                //发送失败的处理
                log.info(topic + " - 生产者 发送消息失败：" + throwable.getMessage());
            }
            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                //成功的处理
                log.info(topic + " - 生产者 发送消息成功：" + stringObjectSendResult.toString());
            }
        });


    }
}
