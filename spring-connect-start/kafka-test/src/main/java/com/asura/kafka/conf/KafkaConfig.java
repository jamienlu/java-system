package com.asura.kafka.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class KafkaConfig {
    public static final String TOPIC_TEST = "topic_test";
    public static final String TOPIC_GROUP = "topic_group";
    @Value("${kafkamq.topics}")
    public static final String TOPICS = "";

    @Bean
    public Map<String,String> kafkaTopics() {
        return Arrays.stream(TOPICS.split(",", -1)).distinct()
            .collect(Collectors.toMap(key -> key, value -> value));
    }
}
