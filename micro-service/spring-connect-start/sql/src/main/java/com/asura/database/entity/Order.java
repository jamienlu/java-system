package com.asura.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Date;

@Repository
@Getter
@Setter
public class Order {
    private String id;
    private BigInteger payment;
    private boolean paymentType;
    private int statue;
    private Date createTime;
    private Date updateTime;
    private Date paymentTime;
    private Date endTime;
    private Date closeTime;
    private int shippingId;
    private int userId;
    private int userNickId;
    private String userMessage;
}
