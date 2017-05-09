package com.lvshandian.lemeng.bean;

import java.io.Serializable;

/**
 * 周榜排行榜实体类
 * Created by Administrator on 2017/2/24.
 */

public class ContributionBeanBack implements Serializable {


    /**
     * picUrl : http://haiyan-news.oss-cn-beijing.aliyuncs.com/29077.png
     * level : 77
     * nickName : 哈哈
     * sumAmount : 343380
     */

    private String picUrl;
    private int level;
    private String nickName;
    private String gender;
    private int sumAmount;
    private String id;

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

    public int getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(int sumAmount) {
        this.sumAmount = sumAmount;
    }

    @Override
    public String toString() {
        return "ContributionBeanBack{" +
                "picUrl='" + picUrl + '\'' +
                ", level=" + level +
                ", nickName='" + nickName + '\'' +
                ", gender='" + gender + '\'' +
                ", sumAmount=" + sumAmount +
                ", id='" + id + '\'' +
                '}';
    }
}
