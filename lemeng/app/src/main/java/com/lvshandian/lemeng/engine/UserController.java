package com.lvshandian.lemeng.engine;

import com.lvshandian.lemeng.MyApplication;
import com.lvshandian.lemeng.entity.AppUser;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

public class UserController {
    private static UserController mInstance = null;
    private AppUser mAppUser = null;

    private UserController() {
    }

    public static synchronized UserController getInstance() {
        if (mInstance == null) {
            mInstance = new UserController();
        }
        return mInstance;
    }

    /**
     * 获取当前用户信息
     *
     * @return
     */
    public AppUser getAppUser() {
        if (mAppUser == null) {
            mAppUser = SharedPreferenceUtils.getUserInfo(MyApplication.mContext);
        }
        return mAppUser;
    }

    public void resetAppUser() {
        mAppUser = null;
    }


}
