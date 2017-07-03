package com.lvshandian.lemeng.entity.lemeng;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/20.
 */

public class Args implements Serializable {
    private int type;
    private int gender;
    private String city;

    public Args(int type, int gender, String city) {
        this.type = type;
        this.gender = gender;
        this.city = city;
    }

    public Args() {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
