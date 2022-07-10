package com.asura.provider.api;

import com.asura.services.Order;
import com.asura.services.OrderService;

import java.io.Serializable;

public class OrderServiceImpl implements OrderService, Serializable {

    @Override
    public Order findOrderById(int id) {
        return new Order(id, "Cuijing" + System.currentTimeMillis(), 9.9f);
    }
}
