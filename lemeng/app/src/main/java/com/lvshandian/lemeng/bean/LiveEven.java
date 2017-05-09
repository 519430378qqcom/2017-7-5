package com.lvshandian.lemeng.bean;

import java.io.Serializable;

/**
 * Created by zhang on 2017/3/3.
 */

public class LiveEven implements Serializable {

    private String id;
    private String roomId;
    private String userId;
    private String nickName;
    private String userAvatar;
    private String publishUrl;
    private String broadcastUrl;
    private String amount;
    private String type;
    private String status;
    private String applyTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getPublishUrl() {
        return publishUrl;
    }

    public void setPublishUrl(String publishUrl) {
        this.publishUrl = publishUrl;
    }

    public String getBroadcastUrl() {
        return broadcastUrl;
    }

    public void setBroadcastUrl(String broadcastUrl) {
        this.broadcastUrl = broadcastUrl;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    @Override
    public String toString() {
        return "LiveEven{" +
                "id='" + id + '\'' +
                ", roomId='" + roomId + '\'' +
                ", userId='" + userId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", publishUrl='" + publishUrl + '\'' +
                ", broadcastUrl='" + broadcastUrl + '\'' +
                ", amount='" + amount + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", applyTime='" + applyTime + '\'' +
                '}';
    }
}
