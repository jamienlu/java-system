package com.asura.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@Getter
@Setter
public class Spu {
    private int id;
    private String title;
    private String subTile;
    private int cid1;
    private int cid2;
    private int cid3;
    private int brandId;
    private boolean saleable;
    private boolean valid;
    private Date createTime;
    private Date updateTime;
}
