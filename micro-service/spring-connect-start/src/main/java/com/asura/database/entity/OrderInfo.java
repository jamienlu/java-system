package com.asura.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class OrderInfo {
    private String orderId;
    private String itemId;
    private int itemCount;
    private double price;
}
