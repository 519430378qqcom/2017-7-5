package com.lvshandian.lemeng.interfaces;

import com.lvshandian.lemeng.entity.GrabPackageBean;
import com.lvshandian.lemeng.entity.PackageRank;

/**
 * author : Cui Dong
 * e-mail : dgsimle@sina.com
 * time   : 2017/6/20
 * version: 1.0
 * desc   : 红包接口
 */
public interface IRedPackageContract {
    interface IView{
        /**
         * 抢红包接口的回调
         * @param grabPackageBean 红包结果
         */
        void grabPackage(GrabPackageBean grabPackageBean);

        /**
         * 红包排行接口的回调
         * @param packageRank   红包排行信息
         */
        void packageRank(PackageRank packageRank);
    }
    interface Presenter{
        /**
         * 抢红包接口方法
         * @param userId    用户id
         * @param redenvelope   红包标识，后台发送
         */
        void grabPackage(String userId,String redenvelope);

        /**
         * 红包排行接口
         * @param redenvelope  红包标识
         */
        void packageRank(String redenvelope);
    }
}
