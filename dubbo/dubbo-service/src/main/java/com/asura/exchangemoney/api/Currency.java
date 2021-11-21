package com.asura.exchangemoney.api;

import java.io.Serializable;
/**
 * 货币表
 */
public class Currency implements Serializable {
    private int id;
    /**
     * 币种名称
     */
    private String name;
    /**
     * 币种 0-RNB
     */
    private int type = 0 ;
    /**
     * 兑换成标准币种的比例
     */
    private float rate =1.0f;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
