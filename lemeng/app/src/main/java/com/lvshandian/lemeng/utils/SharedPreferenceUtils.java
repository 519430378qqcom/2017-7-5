package com.lvshandian.lemeng.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;

/**
 * SP储存类
 * Created by shangshuaibo on 2017/03/07 10:52
 */
public class SharedPreferenceUtils {

    public static final String FILE_NAME = "tangren";  //文件名
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

    public static void saveGoldCoin(Context context, String goldCoin) {
        editor.putString("goldCoin", goldCoin);
        editor.commit();
    }

    public static String getGoldCoin(Context context) {
        return sp.getString("goldCoin", "0");
    }

    public static void saveZhuboGoldCoin(Context context, String goldCoin) {
        editor.putString("zhuboGoldCoin", goldCoin);
        editor.commit();
    }

    public static String getZhuboGoldCoin(Context context) {
        return sp.getString("zhuboGoldCoin", "0");
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
