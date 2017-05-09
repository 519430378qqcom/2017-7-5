package com.lvshandian.lemeng.moudles.mine.bean;

import java.io.Serializable;

/**
 * Created by ssb on 2017/5/9 18:50.
 * company: lvshandian
 */

public class StateCodeBean implements Serializable {

    private String id;
    private String countries;//国家名称英文
    private String countryName;//国家名称中文
    private String countryTop;//手机开头号码
    private String note;//注释

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountries() {
        return countries;
    }

    public void setCountries(String countries) {
        this.countries = countries;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryTop() {
        return countryTop;
    }

    public void setCountryTop(String countryTop) {
        this.countryTop = countryTop;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "StateCodeBean{" +
                "id='" + id + '\'' +
                ", countries='" + countries + '\'' +
                ", countryName='" + countryName + '\'' +
                ", countryTop='" + countryTop + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
