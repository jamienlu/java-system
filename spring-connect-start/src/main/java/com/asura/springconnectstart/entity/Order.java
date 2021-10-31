package com.asura.springconnectstart.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class Order {
    private String orderId;
    private String userId;
    private int statue;
    private double totalPrice;
}
