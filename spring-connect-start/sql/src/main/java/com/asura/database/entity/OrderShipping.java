package com.asura.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.Date;
@Repository
@Getter
@Setter
public class OrderShipping {
    private String id;
    private String orderId;
    private String shipOrz;
    private String shipRealId;
    private String phone;
    private String receiveName;
    private Date creatTime;
    private Date updateTime;
    private String address;
    private int statue;
}
