package com.asura.shradingspheretest;

import com.asura.shradingspheretest.dao.OrderDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class Work1Test {
    @Autowired
    private OrderDao orderDao;

    @Test
    /**
     * 一句一句插入大约100min
     */
    public void addOrder() {
        orderDao.insertTest();
    }
}
