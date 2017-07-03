package com.lvshandian.lemeng.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gjj on 2016/12/20.
 */

public class ProvinceCity implements Serializable {


    /**
     * name : 河北
     * cities : ["石家庄","秦皇岛","廊坊","保定","邯郸","唐山","邢台","衡水","张家口","承德","沧州","衡水"]
     */

    private String name;
    private List<String> cities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }
}
