package com.asura.xa.service;

import com.asura.xa.dao.OrderDao;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;

    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.XA)
    public void insert(int start, int end) {
        orderDao.insert(start, end);
    }

    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.XA)
    public void clear() {
        orderDao.insertRollBack();
    }


    public int selectAll() {
        return orderDao.selectAll();
    }

    public void clear(int userId) {
        orderDao.clear(userId);
    }
}

