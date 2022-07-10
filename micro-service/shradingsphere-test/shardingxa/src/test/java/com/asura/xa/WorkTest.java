package com.asura.xa;

import com.asura.xa.service.OrderService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;


@RunWith(SpringRunner.class)
@SpringBootTest
public class WorkTest {
    @Autowired
    private OrderService orderService;

    @Test
    public void addOrder() {
        orderService.insert(1,2000);
        System.out.println("first select all count:" + orderService.selectAll());;
        try {
            orderService.insert(1000,3000);
        } catch (Exception e) {
            System.out.println("errOr" + e.getMessage());
        }
        System.out.println("second select all count:" + orderService.selectAll());;;
    }

    @After
    public void clearOrder() {
       orderService.clear();
    }
}
