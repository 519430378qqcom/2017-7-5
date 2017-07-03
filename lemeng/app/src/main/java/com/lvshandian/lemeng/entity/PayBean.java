package com.lvshandian.lemeng.entity;

import java.io.Serializable;

/**
 * Created by zhang on 2017/3/16.
 */

public class PayBean implements Serializable{


    /**
     * amount : 1
     * id : 11
     * productId : QUICK_MSECURITY_PAY
     * rechargeTime : 2017-03-16 13:51:14
     * rechargeType : 1
     * status : 0
     * transactionId : 1489643473972124013
     * userId : 1000008
     */

    private int amount;
    private int id;
    private String productId;
    private String rechargeTime;
    private int rechargeType;
    private int status;
    private String transactionId;
    private int userId;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getRechargeTime() {
        return rechargeTime;
    }

    public void setRechargeTime(String rechargeTime) {
        this.rechargeTime = rechargeTime;
    }

    public int getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(int rechargeType) {
        this.rechargeType = rechargeType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
