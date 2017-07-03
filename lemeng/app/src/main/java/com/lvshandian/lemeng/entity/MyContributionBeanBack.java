package com.lvshandian.lemeng.entity;

import java.io.Serializable;

/**
 * 我的贡献榜实体类
 * Created by Administrator on 2017/2/25.
 */

public class MyContributionBeanBack implements Serializable {

    private String picUrl;
    private int level;
    private String nickName;
    private String gender;
    private String id;
    private int contributeCoin;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getContributeCoin() {
        return contributeCoin;
    }

    public void setContributeCoin(int contributeCoin) {
        this.contributeCoin = contributeCoin;
    }

    @Override
    public String toString() {
        return "MyContributionBeanBack{" +
                "picUrl='" + picUrl + '\'' +
                ", level=" + level +
                ", nickName='" + nickName + '\'' +
                ", gender='" + gender + '\'' +
                ", id='" + id + '\'' +
                ", contributeCoin=" + contributeCoin +
                '}';
    }
}
