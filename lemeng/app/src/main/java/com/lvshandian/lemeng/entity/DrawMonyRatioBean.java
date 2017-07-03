package com.lvshandian.lemeng.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/23.
 */

public class DrawMonyRatioBean implements Serializable {

    /**
     * createdTime : 2017-03-17 15:31:05
     * exchangeAmount : 1
     * exchangeGold : 100
     * exchangeLeastAmount : 10
     * exchangeMaxAmount : 2000
     * id : 6
     * type : 1
     */

    private String createdTime;
    private String exchangeAmount;
    private String exchangeGold;
    private String exchangeLeastAmount;
    private String exchangeMaxAmount;
    private String id;
    private String type;

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getExchangeAmount() {
        return exchangeAmount;
    }

    public void setExchangeAmount(String exchangeAmount) {
        this.exchangeAmount = exchangeAmount;
    }

    public String getExchangeGold() {
        return exchangeGold;
    }

    public void setExchangeGold(String exchangeGold) {
        this.exchangeGold = exchangeGold;
    }

    public String getExchangeLeastAmount() {
        return exchangeLeastAmount;
    }

    public void setExchangeLeastAmount(String exchangeLeastAmount) {
        this.exchangeLeastAmount = exchangeLeastAmount;
    }

    public String getExchangeMaxAmount() {
        return exchangeMaxAmount;
    }

    public void setExchangeMaxAmount(String exchangeMaxAmount) {
        this.exchangeMaxAmount = exchangeMaxAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
