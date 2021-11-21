package com.asura.exchangemoney.api;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户货币表
 */
public class UserAccount implements Serializable {
    private int id;
    private long userId;
    private int currencyType;
    private float num;
    private Date startTime;
    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(int currencyType) {
        this.currencyType = currencyType;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "id=" + id +
                ", userId=" + userId +
                ", currencyType=" + currencyType +
                ", num=" + num +
                ", startTime=" + startTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
