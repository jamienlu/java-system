package com.asura.xa;

import com.asura.xa.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class Work2Test {
    @Autowired
    private OrderService orderService;

    @Test
    /**
     * 一句一句插入大约100min
     */
    public void addOrder() {
        orderService.insert();
    }
}
