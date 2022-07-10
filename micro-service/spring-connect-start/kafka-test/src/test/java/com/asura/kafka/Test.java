package com.asura.kafka;

import com.asura.kafka.jms.KafkaProducer;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class Test {
    @Autowired
    private KafkaProducer kafkaProducer;

    @org.junit.Test
    public void test() {
        kafkaProducer.produce("topic1","hello1 world1!");
        kafkaProducer.produce("","hello world!");
        kafkaProducer.produce("topic2","hello2 world2!");
    }
}
