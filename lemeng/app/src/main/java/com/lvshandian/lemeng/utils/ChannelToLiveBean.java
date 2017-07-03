package com.lvshandian.lemeng.utils;


/**
 * Created by sll on 2016/12/22.
 */

import com.lvshandian.lemeng.entity.LiveFamilyMemberBean;
import com.lvshandian.lemeng.entity.LiveListBean;

/**
 * 将频道等里的网络请求返回类转成liveBean
 *
 * @author sll
 * @time 2016/12/22 16:53
 */
public class ChannelToLiveBean {
    /**
     * 根据频道的实体类，获取liveBean用于跳转到watchLive
     *
     * @author sll
     * @time 2016/12/22 11:18
     */
    public static LiveListBean getLiveBeanFromObj(LiveFamilyMemberBean.ObjBean obj) {
        LiveListBean liveListBean = new LiveListBean();
        liveListBean.setLivePicUrl(obj.getLivePicUrl());
        liveListBean.setPicUrl(obj.getPicUrl());
        liveListBean.setAddress(obj.getAddress());
        liveListBean.setNickName(obj.getNickName());
        liveListBean.setId(Integer.parseInt(obj.getId() + ""));
        liveListBean.setRoomId(obj.getRoomId());
        LiveListBean.RoomsBean creatorBean = new LiveListBean.RoomsBean();
        creatorBean.setCity(obj.getRooms().getCity());
        creatorBean.setId(obj.getRooms().getId());
        creatorBean.setOnlineUserNum(obj.getRooms().getOnlineUserNum());
        creatorBean.setUserId(obj.getId());
        creatorBean.setRoomId(obj.getRooms().getRoomId());
        creatorBean.setBroadcastUrl(obj.getRooms().getBroadcastUrl());
        creatorBean.setPublishUrl(obj.getRooms().getPublishUrl());

        if (!android.text.TextUtils.isEmpty(obj.getRooms().getPrivateFlag())) {
            creatorBean.setPrivateFlag(Integer.parseInt(obj.getRooms().getPrivateFlag().trim()));//是否是私密房间1是2否
        }
        if (!android.text.TextUtils.isEmpty(obj.getRooms().getRoomPay())) {
            creatorBean.setRoomPay(Integer.parseInt(obj.getRooms().getRoomPay()));//私密直播需要的金币
        }
        creatorBean.setRoomPw(obj.getRooms().getRoomPw());//私密直播需要的密码
        liveListBean.setRooms(creatorBean);
        return liveListBean;
    }
}
