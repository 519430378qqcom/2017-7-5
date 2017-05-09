package com.lvshandian.lemeng.bean;

import com.lvshandian.lemeng.db.Audios;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/24.
 */

public class PlayerSearchBean implements Serializable {

    private List<Audios> song;
    /**
     * error_code : 22000
     * order : song
     */
    private int error_code;
    private String order;

    public List<Audios> getSong() {
        return song;
    }

    public void setSong(List<Audios> song) {
        this.song = song;
    }


    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
