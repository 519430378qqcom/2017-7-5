package com.lvshandian.lemeng.bean;

/**
 * author : Cui Dong
 * e-mail : dgsimle@sina.com
 * time   : 2017/6/26
 * version: 1.0
 * desc   : 全局开关EvenBus发送的bean
 */
public class GlobalSwitch {
    public GlobalSwitch(boolean playSwitch, boolean gameSwitch) {
        this.playSwitch = playSwitch;
        this.gameSwitch = gameSwitch;
    }

    public GlobalSwitch() {
    }

    public boolean playSwitch;
    public boolean gameSwitch;
}
