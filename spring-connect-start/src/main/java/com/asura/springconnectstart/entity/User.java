package com.asura.springconnectstart.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class User {
    private String userId;
    private String name;
    private String phone;
    private String email;
    private long regisTime;
    private int statu;
}
