package com.lvshandian.lemeng.bean;

import java.io.Serializable;

/**
 * Created by sll on 2016/12/2.
 */

public class RoomUserBean implements Serializable {
    private String picUrl;
    private String gender;
    private String level;
    private String signature;
    private String nickName;
    private String vip;
    private String userId;
    private String roomId;
    private String address;
    private String verified;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "RoomUserBean{" +
                "picUrl='" + picUrl + '\'' +
                ", gender='" + gender + '\'' +
                ", level='" + level + '\'' +
                ", signature='" + signature + '\'' +
                ", nickName='" + nickName + '\'' +
                ", vip='" + vip + '\'' +
                ", userId='" + userId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", address='" + address + '\'' +
                ", verified='" + verified + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof RoomUserBean)) {
            return false;
        }
        final RoomUserBean person = (RoomUserBean) obj;
        if (this.getUserId().equals(person.getUserId())) {
            return true;
        } else {
            return false;
        }

    }
}
