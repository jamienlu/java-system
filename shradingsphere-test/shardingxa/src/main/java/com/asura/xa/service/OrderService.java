package com.asura.xa.service;

import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Service
public class OrderService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @ShardingTransactionType(TransactionType.XA)
    public void insert() {
        String sql = "insert into asura_order(id,payment,payment_type,status," +
                "create_time,update_time,payment_time,end_time,close_time," +
                "shipping_id,user_id,user_message,user_nick_id)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        long time1 = System.currentTimeMillis();
        List<Object[]> batchObjects = new ArrayList<>();
        IntStream.range(1,5000).forEach(serNo -> {
            Object[] obj = new Object[]{serNo, 100, 1, 1,
                    new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                    new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()),new Date(System.currentTimeMillis()),
                    serNo * 11, new Random().nextInt(100), "hello",100};
            batchObjects.add(obj);
        });
        jdbcTemplate.batchUpdate(sql, batchObjects);
        System.out.println("addUserBatch cost time:" + (System.currentTimeMillis() - time1)/1000);
    }
}

