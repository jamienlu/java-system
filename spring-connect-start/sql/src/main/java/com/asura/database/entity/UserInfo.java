package com.asura.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class UserInfo {
    private int id;
    private int userId;
    private String userDesc;
    private int score;
}


