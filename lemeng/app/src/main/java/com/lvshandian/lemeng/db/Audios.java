package com.lvshandian.lemeng.db;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/24.
 */

public class Audios extends DataSupport implements Serializable{
    private String bitrate_fee;
    private String weight;
    private String songname;
    private String songid;
    private String has_mv;
    private String yyr_artist;
    private String artistname;
    private String resource_type_ext;
    private String resource_provider;
    private String control;
    private String encrypted_songid;
    private boolean isDownload;
    private boolean isPlaying;
    private boolean isPause;

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public String getBitrate_fee() {
        return bitrate_fee;
    }

    public void setBitrate_fee(String bitrate_fee) {
        this.bitrate_fee = bitrate_fee;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public String getSongid() {
        return songid;
    }

    public void setSongid(String songid) {
        this.songid = songid;
    }

    public String getHas_mv() {
        return has_mv;
    }

    public void setHas_mv(String has_mv) {
        this.has_mv = has_mv;
    }

    public String getYyr_artist() {
        return yyr_artist;
    }

    public void setYyr_artist(String yyr_artist) {
        this.yyr_artist = yyr_artist;
    }

    public String getArtistname() {
        return artistname;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
    }

    public String getResource_type_ext() {
        return resource_type_ext;
    }

    public void setResource_type_ext(String resource_type_ext) {
        this.resource_type_ext = resource_type_ext;
    }

    public String getResource_provider() {
        return resource_provider;
    }

    public void setResource_provider(String resource_provider) {
        this.resource_provider = resource_provider;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getEncrypted_songid() {
        return encrypted_songid;
    }

    public void setEncrypted_songid(String encrypted_songid) {
        this.encrypted_songid = encrypted_songid;
    }
}
