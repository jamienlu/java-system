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
    public void insert() {
        orderDao.insert();
        orderDao.select(10);
        orderDao.clear(10);
        orderDao.select(10);
        orderDao.select(11);
    }

    @Transactional(rollbackFor = Exception.class)
    @ShardingTransactionType(TransactionType.XA)
    public void clear() {
        orderDao.insertRollBack();
    }

    public void clear(int userId) {
        orderDao.clear(userId);
    }
}

