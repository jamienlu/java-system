package com.asura.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@Getter
@Setter
public class Sku {
    private int id;
    private int spuId;
    private String pics;
    private String indexs; // 特有规格属性在spu属性模板中的对应下标组合
    private String ownSpecs; // sku的特有规格参数键值对
    private boolean enable;
    private Date creatTime;
    private Date updateTime;
}
