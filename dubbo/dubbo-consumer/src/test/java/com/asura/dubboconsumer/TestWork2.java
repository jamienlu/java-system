package com.asura.dubboconsumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestWork2 {
    @Autowired
    private com.asura.dubboconsumer.test.Test test;

    @Test
    public void test() {
        test.test();
    }
}
