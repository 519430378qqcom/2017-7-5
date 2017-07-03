package com.lvshandian.lemeng.entity;

import java.io.Serializable;

/**
 * 同意连麦自定义消息
 * Created by Administrator on 2017/3/20.
 */

public class CustomLianmaiBean implements Serializable {
    private String watch_private_flag;
    private String userId;
    private String type;
    private String vip;
    private String broadcastUrl;
    private String picUrl;

    private String amount;
    private String applyTime;
    private String id;
    private String nickName;
    private String publishUrl;
    private String roomId;
    private String status;
    private String userAvatar;

    public String getWatch_private_flag() {
        return watch_private_flag;
    }

    public void setWatch_private_flag(String watch_private_flag) {
        this.watch_private_flag = watch_private_flag;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getBroadcastUrl() {
        return broadcastUrl;
    }

    public void setBroadcastUrl(String broadcastUrl) {
        this.broadcastUrl = broadcastUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPublishUrl() {
        return publishUrl;
    }

    public void setPublishUrl(String publishUrl) {
        this.publishUrl = publishUrl;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    @Override
    public String toString() {
        return "CustomLianmaiBean{" +
                "watch_private_flag='" + watch_private_flag + '\'' +
                ", userId='" + userId + '\'' +
                ", type='" + type + '\'' +
                ", vip='" + vip + '\'' +
                ", broadcastUrl='" + broadcastUrl + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", amount='" + amount + '\'' +
                ", applyTime='" + applyTime + '\'' +
                ", id='" + id + '\'' +
                ", nickName='" + nickName + '\'' +
                ", publishUrl='" + publishUrl + '\'' +
                ", roomId='" + roomId + '\'' +
                ", status='" + status + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                '}';
    }
}
