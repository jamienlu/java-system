package com.asura.activemq;

import com.asura.activemq.dao.MessageTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestAcitveMq {
    @Autowired
    private MessageTemplate messageTemplate;

    @Test
    public void test() {
        String msg1 = "hello world queue!";
        messageTemplate.produceMsg(msg1);
        String msg2 = "hello world topic!";
        // active接受topic需要修改监听器工厂
        messageTemplate.produceTopicMsg(msg2);
    }
}
