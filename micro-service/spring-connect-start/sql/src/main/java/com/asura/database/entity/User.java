package com.asura.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@Getter
@Setter
public class User {
    private int id;
    private String name;
    private Date createTime;
    private Date updateTime;
    private String phone;
    private String cardId;
    private String realName;
    private boolean statue;
}
