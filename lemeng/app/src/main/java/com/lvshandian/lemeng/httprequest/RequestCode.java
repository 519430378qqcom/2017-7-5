package com.lvshandian.lemeng.httprequest;

/**
 * author: newlq on 2016/9/1 17:08.
 * email： @lvshandian.com
 * company:北京绿闪电科技有限公司
 * details：请求常量类， 用于判断请求与handler 回调的msg.what 的值 是否是统一请求
 */
public interface RequestCode {

    int SELECT_USER = 999;     //查询用户信息
    int LOGIN_TAG = 1000;     //登录
    int REGISTER_TAG = 1001;     //注册
    int FORGETPSWD_TAG = 10011;     //忘记密码
    int USER_TAG = 1002; //修改用户信息
    int START_LIVE = 1004;  //开启直播
    int MY_PHOTO_LOAD = 1006; //请求阿里云id、key
    int ATTENTION_LIVE = 1007;//关注人的列表
    int SEACH_USER = 1008;//搜索用户
    int ATTENTION_USER = 1008;//关注人的列表
    int IF_ATTENTION = 1009;//判断是否被关注过
    int GET_GIFT = 1010;//获取礼物列表
    int SEND_GIFT = 1011;//赠送礼物
    int ROOM_LIANMAI = 1012;//连麦
    int REQUEST_ROOM_CLOES = 1013;
    int GET_CODE = 1014; //获取验证码
    int HOT_LIVE = 1015;//直播热门列表
    int ACCEPT_LIANMAI = 1018;//观众接受主播连麦
    int REQUEST_LIANMAI_ID = 1019;//观众同意连麦生成Id
    int REQUEST_LIANMAI_ID1= 10191;//将我的状态变成未连麦状态
    int REQUEST_LIANMAI_LIVE = 1020;//生成推拉流
    int IS_REQUEST_LIANMAI_LIVE = 1022;//生成推拉流
    int MY_PHOTO_UPLOAD_CODE = 4001; //上传图片
    int MY_PHOTO_DELETE_CODE = 4002; //删除图片
    int MY_VIDEO_LOAD = 4003; //视频列表
    int MY_VIDEO_UPLOAD = 4004; //上传视频
    int MYCONTROLLER = 4005; //上传视频


    int HOME_BANNER = 2999;//banner图列表
    int MODIFY_PASSWORD = 3000;//修改密码
    int REAL_NAME_VERTIFY = MODIFY_PASSWORD + 1;//实名认证

    int REQUEST_USER_INFO = 2001;//请求用户信息
    int REQUEST_ROOM_JOIN = 2008;//加入直播间
    int REQUEST_ROOM_LIVE = 2009;//主播获取全部申请列表
    int REQUEST_ROOM_EXIT = 2010;//退出直播间
    int REQUEST_REPORT = 2015;//举报
    int TIMERLIVE = 2017;//主播获取全部申请列表
    int SELECT_LEVEL = 2018;//查询等级信息
    int REQUEST_RANK = 2020;//查询排行榜/贡献榜
    int DRAW_MONEY = 2021;//提现
    int DRAW_MONEY_RATIO= 2022;//提现比例
    int SELECTE_QUIT = 2024;//退出直播间
    int ROOMLIVEEXIT = 2025;//客户退出连线
    int REQUEST_LIANMAI_LIVE_AAL = 2026;//观众同意连麦获取推拉流地址
    int BIND_WEICHAT = 2027;//绑定微信公众号

}
