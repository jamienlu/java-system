package com.asura.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
@Getter
@Setter
public class ShopCar {
    private String id;
    private int userId;
    private int skuId;
    private int num;
    private BigInteger price;
    private String pic;
    private BigInteger totalPrice;
}
