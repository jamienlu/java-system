package com.asura.xa.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class OrderDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(int start, int end) {
        String sql = "insert into share_order(order_id,user_id,order_name,order_time) values(?,?,?,?)";
        long time1 = System.currentTimeMillis();
        List<Object[]> batchObjects = new ArrayList<>();
        IntStream.range(start,end).forEach(serNo -> {
            Object[] obj = new Object[]{serNo, new Random().nextInt(10), "orderName" + serNo, new Timestamp(Date.from(LocalDateTime.now().toInstant(ZoneOffset.of("+8"))).getTime())};
            batchObjects.add(obj);
        });
        jdbcTemplate.batchUpdate(sql, batchObjects);
        System.out.println("insert batch cost time:" + (System.currentTimeMillis() - time1)/1000);
    }
    public int selectAll() {
        String sql = "select count(1) from share_order";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    public void select(int userId) {
        String sql = "select count(1) from share_order where user_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[]{userId});
        System.out.println("select userid = " + userId + "__count:" + count);
    }

    public void clear(int userId) {
        String sql = "delete from share_order where user_id = ?";
        int count = jdbcTemplate.update(sql, new Object[]{userId});
        System.out.println("delete userid = " + userId + "__count:" + count);
    }

    public void insertRollBack() {
        String sql = "delete from share_order";
        jdbcTemplate.update(sql);
        System.out.println("clear userid 0 - 100 ");
    }

    public void commit() throws SQLException {
        jdbcTemplate.getDataSource().getConnection().commit();
    }
}
