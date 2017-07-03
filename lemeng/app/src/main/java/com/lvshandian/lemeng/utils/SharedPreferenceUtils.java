package com.lvshandian.lemeng.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.lvshandian.lemeng.entity.AppUser;
import com.lvshandian.lemeng.entity.mine.LoginFrom;

import java.util.Map;

/**
 * SP储存类
 * Created by shangshuaibo on 2017/03/07 10:52
 */
public class SharedPreferenceUtils {

    public static final String FILE_NAME = "lemeng";  //文件名
    private static SharedPreferenceUtils instance;
    private static SharedPreferences sp;
    private static Editor editor;

    /**
     * 单例模式
     *
     * @return
     */
    public static SharedPreferenceUtils getInstance() {
        if (instance == null) {
            instance = new SharedPreferenceUtils();
        }
        return instance;
    }

    /**
     * 初始化工具类，在application中配置
     *
     * @param context
     */
    public void init(Context context) {
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }


    public static void saveUserInfo(Context context, AppUser appUser) {
        editor.putString("shareCode", appUser.getShareCode());
        editor.putString("spendGoldCoin", appUser.getSpendGoldCoin());
        editor.putString("gender", appUser.getGender());
        editor.putString("signature", appUser.getSignature());
        editor.putString("goldCoin", appUser.getGoldCoin());
        editor.putString("phoneNum", appUser.getPhoneNum());
        editor.putString("neteaseAccount", appUser.getNeteaseAccount());
        editor.putString("roomId", appUser.getRoomId());
        editor.putString("points", appUser.getPoints());
        editor.putString("picUrl", appUser.getPicUrl());
        editor.putString("exchangeStatus", appUser.getExchangeStatus());
        editor.putString("exchangeCash", appUser.getExchangeCash());
        editor.putString("id", appUser.getId());
        editor.putString("vip", appUser.getVip());
        editor.putString("live", appUser.getLive());
        editor.putString("gradeSatisfied", appUser.getGradeSatisfied());
        editor.putString("address", appUser.getAddress());
        editor.putString("fansNum", appUser.getFansNum());
        editor.putString("level", appUser.getLevel());
        editor.putString("nickName", appUser.getNickName());
        editor.putString("verified", appUser.getVerified());
        editor.putString("neteaseToken", appUser.getNeteaseToken());
        editor.putString("userName", appUser.getUserName());
        editor.putString("exchangeGoldCoin", appUser.getExchangeGoldCoin());
        editor.putString("regTime", appUser.getRegTime());
        editor.putString("followNum", appUser.getFollowNum());
        editor.putString("serviceSatisfied", appUser.getServiceSatisfied());
        editor.putString("payForVideoChat", appUser.getPayForVideoChat());
        editor.putString("livePicUrl", appUser.getLivePicUrl());
        editor.putString("online", appUser.getOnline());
        editor.putString("location", appUser.getLocation());
        editor.putString("receivedGoldCoin", appUser.getReceivedGoldCoin());
        editor.putString("status", appUser.getStatus());
        editor.putString("deviceToken", appUser.getDeviceToken());
        editor.putString("identityCode", appUser.getIdentityCode());
        editor.putString("introducerCode", appUser.getIntroducerCode());
        editor.putString("constellation", appUser.getConstellation());
        editor.putString("loginStatus", appUser.getLoginStatus());
        editor.putString("differentStatus", appUser.getDifferentStatus());
        editor.commit();
    }


    public static AppUser getUserInfo(Context context) {
        AppUser appUser = new AppUser();
        appUser.setShareCode(sp.getString("shareCode", ""));
        appUser.setSpendGoldCoin(sp.getString("spendGoldCoin", ""));
        appUser.setGender(sp.getString("gender", ""));
        appUser.setSignature(sp.getString("signature", ""));
        appUser.setGoldCoin(sp.getString("goldCoin", ""));
        appUser.setPhoneNum(sp.getString("phoneNum", ""));
        appUser.setNeteaseAccount(sp.getString("neteaseAccount", ""));
        appUser.setRoomId(sp.getString("roomId", ""));
        appUser.setPoints(sp.getString("points", ""));
        appUser.setPicUrl(sp.getString("picUrl", ""));
        appUser.setExchangeStatus(sp.getString("exchangeStatus", ""));
        appUser.setExchangeCash(sp.getString("exchangeCash", ""));
        appUser.setId(sp.getString("id", ""));
        appUser.setVip(sp.getString("vip", ""));
        appUser.setLive(sp.getString("live", ""));
        appUser.setGradeSatisfied(sp.getString("gradeSatisfied", ""));
        appUser.setAddress(sp.getString("address", ""));
        appUser.setFansNum(sp.getString("fansNum", ""));
        appUser.setLevel(sp.getString("level", ""));
        appUser.setNickName(sp.getString("nickName", ""));
        appUser.setVerified(sp.getString("verified", ""));
        appUser.setNeteaseToken(sp.getString("neteaseToken", ""));
        appUser.setUserName(sp.getString("userName", ""));
        appUser.setExchangeGoldCoin(sp.getString("exchangeGoldCoin", ""));
        appUser.setRegTime(sp.getString("regTime", ""));
        appUser.setFollowNum(sp.getString("followNum", ""));
        appUser.setServiceSatisfied(sp.getString("serviceSatisfied", ""));
        appUser.setPayForVideoChat(sp.getString("payForVideoChat", ""));
        appUser.setLivePicUrl(sp.getString("livePicUrl", ""));
        appUser.setOnline(sp.getString("online", ""));
        appUser.setLocation(sp.getString("location", ""));
        appUser.setReceivedGoldCoin(sp.getString("receivedGoldCoin", ""));
        appUser.setStatus(sp.getString("status", ""));
        appUser.setDeviceToken(sp.getString("deviceToken", ""));
        appUser.setIdentityCode(sp.getString("identityCode", ""));
        appUser.setIntroducerCode(sp.getString("introducerCode", ""));
        appUser.setConstellation(sp.getString("constellation", ""));
        appUser.setLoginStatus(sp.getString("loginStatus", ""));
        appUser.setDifferentStatus(sp.getString("differentStatus", ""));
        return appUser;
    }

    public static void saveLoginFrom(Context context, LoginFrom loginFrom) {
        editor.putBoolean("isThirdLogin", loginFrom.isThirdLogin());
        editor.putString("password", loginFrom.getPassword());
        editor.commit();
    }

    public static LoginFrom getLoginFrom(Context context){
        LoginFrom loginFrom = new LoginFrom();
        loginFrom.setThirdLogin(sp.getBoolean("isThirdLogin",false));
        loginFrom.setPassword(sp.getString("password",""));
        return loginFrom;
    }

    public static void saveGoldCoin(Context context, String goldCoin) {
        editor.putString("goldCoin", goldCoin);
        editor.commit();
    }

    public static String getGoldCoin(Context context) {
        return sp.getString("goldCoin", "0");
    }

    public static void saveLevel(Context context,String level){
        editor.putString("level", level);
        editor.commit();
    }

    public static String getLevel(Context context){
        return sp.getString("level","");
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        editor.clear();
        editor.commit();
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        return sp.getAll();
    }


}
