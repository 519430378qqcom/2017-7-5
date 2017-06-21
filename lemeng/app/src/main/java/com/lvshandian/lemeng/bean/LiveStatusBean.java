package com.lvshandian.lemeng.bean;

import java.io.Serializable;

/**
 * Created by ssb on 2017/6/21 10:42.
 * company: lvshandian
 */

public class LiveStatusBean implements Serializable{

    /**
     * id : 1
     * name : 主播
     * fun : anchor
     * state : 1
     */

    private int id;
    private String name;
    private String fun;
    private int state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFun() {
        return fun;
    }

    public void setFun(String fun) {
        this.fun = fun;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "LiveStatusBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fun='" + fun + '\'' +
                ", state=" + state +
                '}';
    }
}
