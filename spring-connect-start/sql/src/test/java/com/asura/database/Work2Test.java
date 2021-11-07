package com.asura.database;

import com.asura.database.dao.OrderDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Work2Test {

    @Autowired
    private OrderDao orderDao;

    @Test
    public void test() {
        //orderDao.insert();
        orderDao.read1();
        //orderDao.read2();
    }
}
