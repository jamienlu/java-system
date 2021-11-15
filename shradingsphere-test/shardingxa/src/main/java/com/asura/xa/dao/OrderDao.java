package com.asura.xa.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class OrderDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
        System.out.println("insert batch cost time:" + (System.currentTimeMillis() - time1)/1000);
    }

    public void select(int userId) {
        String sql = "select count(1) from asura_order where user_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[]{userId});
        System.out.println("select userid = " + userId + "__count:" + count);
    }

    public void clear(int userId) {
        String sql = "delete from asura_order where user_id = ?";
        int count = jdbcTemplate.update(sql, new Object[]{userId});
        System.out.println("delete userid = " + userId + "__count:" + count);
    }

    public void insertRollBack() {
        String sql = "delete from asura_order";
        jdbcTemplate.update(sql);
        System.out.println("clear userid 0 - 100 ");
    }
}
