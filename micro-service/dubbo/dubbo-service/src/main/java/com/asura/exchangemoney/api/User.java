package com.asura.exchangemoney.api;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String name;
    /**
     * 以标准计算的数量
     */
    private float total;

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

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
