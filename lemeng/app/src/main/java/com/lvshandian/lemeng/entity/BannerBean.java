package com.lvshandian.lemeng.entity;

import java.io.Serializable;

/**
 * Created by ssb on 2017/4/14 16:08.
 * company: lvshandian
 */

public class BannerBean implements Serializable {

    /**
     * id : 57
     * picUrl : http://lemeng-news.oss-cn-beijing.aliyuncs.com/lemengImg/1492154955667.png
     * pointUrl :
     * creatTime : 2017-04-14 14:04:48
     * status : 1
     * carouselName : sad
     */

    private int id;
    private String picUrl;
    private String pointUrl;
    private String creatTime;
    private String status;
    private String carouselName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPointUrl() {
        return pointUrl;
    }

    public void setPointUrl(String pointUrl) {
        this.pointUrl = pointUrl;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCarouselName() {
        return carouselName;
    }

    public void setCarouselName(String carouselName) {
        this.carouselName = carouselName;
    }
}
