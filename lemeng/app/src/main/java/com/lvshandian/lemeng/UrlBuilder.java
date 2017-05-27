package com.lvshandian.lemeng;

import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by zhang on 2016/10/11.
 * 创建网络管理类请求，网址管理类；
 */
public class UrlBuilder {
    //乐檬线上服務器
//    public static final String serverUrl = "http://47.88.229.22:80/";// 服务器网址公网
//    public static final String chargeServerUrl = "http://47.88.229.22:80/admin/";// 充值端口
    //乐檬测试服務器
    public static final String serverUrl = "http://60.205.114.36:80/";// 服务器网址公网
    public static final String chargeServerUrl = "http://60.205.114.36:80/admin/";// 充值端口
//    public static final String serverUrl = "http://10.11.1.119:8080/";// 服务器网址公网
//    public static final String chargeServerUrl = "http://10.11.1.119:8081/admin/";// 充值端口
    /*乐檬接口*/
    public static final String HEAD_DEFAULT = "http://lemeng.oss-ap-southeast-1.aliyuncs.com/lemengImg/1495594583610.png";    //当有空头像的时候
    public static final String LOGIN = "/api/v1/login";    //登录
    public static final String REGISTER = "/appusers/register"; //注册
    public static final String forgetPswd = "/appusers/forgetRegister"; //忘记密码
    public static final String getCode = "/appUserControl/sendMSG";  //获取验证码
    public static final String appRooms = "/appRooms/getRooms";// type=1热门，type = 2最新，type ==3 地方北京市直播
    public static final String GET_GIFT = "/appGift/findGift";//获取礼物列表
    public static final String user = "/appusers/update";    //修改用户信息public static final String modifyPass = "/api/v1/user/password";//修改密码 public static final String GET_GIFT = "appGift/findGift";//获取礼物列表
    public static final String modifyPass = "/api/v1/user/password";   //修改密码
    public static final String funseAndFollow = "/api/v1/user/";//粉丝/关注列表
    public static final String ATTENTION_USER = "/api/v1/user/follow";//关注取消关注
    public static final String selectUserInfo = "/appusers/selectUserById";  //查询个人信息
    public static final String selectLevel = "/appLevel/getLevelRate";  //查询当前等级信息
    public static final String Authentication = "/appusers/updateVer";  //实名认证
    public static final String report = "/api/v1/user/report";//举报
//    public static final String weekContribution = "/appusers/getConsumeAmount";//周榜排行榜
    public static final String weekContribution = "/appusers/getConsumeAmountAndroid";//周榜排行榜
//    public static final String myContribution = "/appusers/getUserContribution";//我的贡献榜
    public static final String myContribution = "/appusers/getUserContributionAndroid";//我的贡献榜
    public static final String drawMoney = "/appExchange/updateExchangeCash";//提现
    public static final String drawMoney_ratio = "/appExchange/exchangeRatioUse";//提现比例
    public static final String orderCreate = "/appZfPay/toApppay";//生成订单
    public static final String wxPay = "/appwxpay/prepare";//微信支付
    public static final String selectQuite = "/appusers/getUserById";//主播离开后获取用户信息
    public static final String openRegister = "/appusers/open/register";//第三方登录
    public static final String requestLianmai = "/api/v1/room/live/apply";//主播向观众发起连麦
    public static final String acceptLianmai = "/api/v1/room/live/result";//主播向观众发起连麦
    public static final String selectFamilyMember = "/appusers/selectFamilyMember.do";//家族
    public static final String myController = "appUserControl/getUserControlList";//我的场控列表
    public static final String myControllerAdd = "appUserControl/add";//我的场控添加
    public static final String myControllerDelete = "appUserControl/delete";//我的场控刪除
    public static final String appUserControl = "appUserControl/stopUser";//禁言
    public static final String aliyun = "/aliyun/token";    //请求阿里云id、key
    public static final String START = "/api/v1/room/create/";//新建直播/api/v1/room/create
    public static final String SEARCH = "/api/v1/user/find";//搜索用户
    public static final String CHAGE_LIANMAI_STATUS = "/api/v1/room/live/apply";//将我的状态变成未连麦状态
    public static final String SEARCH_CONTROLLER = "/appusers/find";//搜索用户场控
    public static final String WEICHAT_BIND = "/api/v1/exchange/bind";//绑定微信公众号
    public static final String YINLIAN_PAY_WEB = "http://59.110.17.224:80/admin/static/payment.html?userId=%s";//银联支付web
    public static final String BAIDU_SONG_SEARCH = "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&from=webapp_music&method=baidu.ting.search.catalogSug&query=%s";//搜索
    public static final String BAIDU_SONG_PLAY = "http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&from=webapp_music&method=baidu.ting.song.play&songid=%s";//得到下载的url
    public static final String getBanner = "/appCarouselFigure/carouselList";//得到banner图
    public static final String ChangeRooms = "/appRooms/getnewRoom";//切换房间
    public static final String getStateCode = "/appusers/getcountry";//获得国家代码
    public static final String room_funse = "/appRooms/findFansOnline";//直播间的粉丝列表


    public static final String IF_ATTENTION = "/appusers/user/info";//请求别人信息
    public static final String SEND_GIFT = "/appRooms/reward";//送礼物
    public static final String joinRoom = "/api/v1/room/";//加入直播间
    public static final String ifEnter = "/appRooms/ifEnter";//判断是否进入过该房间
    public static final String updateCoin = "/appRooms/updateCoin";//支付进入
    public static final String join = "/appRooms/join";//进入直播间
    public static final String MY_PHOTO_UPLOAD = "/api/v1/user/album/pic";//我的界面图片上传
    public static final String MY_VIDEO_UPLOAD = "/api/v1/user/album/video";//我的界面图片上传


    public static final String room = "/api/v1/room/";//聊天室头像列表
    public static final String barrage = "/api/v1/barrage/";//发送弹幕消息扣金币
    public static final String roomExit = "/api/v1/room/exit";//退出直播间

    public static final String getTimenumber = "/appusers/gettimenumber";//获取上期开奖数据

    public static final String reciveAmount = "/appRooms/reciveAmount";//投注

    public static final String getBl = "/admin/appusers/getCathecticRate";//开游戏、查询投注比例


    //阿里云
    public static final String ALIYUN_IMG = "http://lemeng.oss-ap-southeast-1.aliyuncs.com/";//阿里云图片視頻生成地址
    public static final String OSS_ENDPOINT = "http://oss-ap-southeast-1.aliyuncs.com";//阿里云OSS_ENDPOINT
    public static final String BUCKET_NAME = "lemeng";// 阿里云BUCKET_NAME  OSS
    //------------------------------------斗牛游戏接口----------------------------------------
    /**
     * 斗牛开始，get;params（roomId）
     */
    public static final String BULLFIGHT_START = "admin/appusers/openPlay";
    /**
     * 获取倒计时，get,params(roomId)
     */
    public static final String BULLFIGHT_TIMER = "admin/appusers/Countdowntime";
    /**
     * 初始化游戏结果25秒调用，get param（roomId）
     */
    public static final String BULLFIGHT_INITGAME = "admin/appusers/anchor25s";
    /**
     * 获取庄家信息，get
     */
    public static final String BULLFIGHT_BANKER = "admin/appusers/getbankerUser";
    /**
     * 投注接口，get param（userId，roomId，amount，type
     ，perid
     ，status）
     */
    public static final String BULLFIGHT_BETTING = "admin/appusers/bugtaurus";

    /**
     * 生成投注url
     * @param userId
     * @param roomId
     * @param amount 投注金额
     * @param type  1为投注天2为投注地3为人
     * @param perid 期数
     * @param status 0 为1倍 1为2倍
     * @return
     */
    public static String betting(int userId, int roomId, int amount, int type, int perid, int status){
        return serverUrl+BULLFIGHT_BETTING+"?userId="+userId+"&roomId="+roomId+"&amount="+amount+"&type="+type+"&perid="+perid+"&status="+status;
    }

    /**
     * 主播（29s）调用重置时间，get,params(roomId,perId)
     */
    public static final String BULLFIGHT_INITTIME = "admin/appusers/changetime";
    /**
     * 开牌结果（20s调用），get,params(roomId)
     */
    public static final String BULLFIGHT_POKER_RESULT = "admin/appusers/cardsresult";
    /**
     * 刷新庄家金币数
     */
    public static final String BULLFIGHT_BANKER_BALANCE = "admin/appusers/getcontrol";
    /**
     * 开奖结果roomId	String 	房间id
        perid	String	期数
        userId	String	用户id
     */
    public static final String BULLFIGHT_RESULT = "admin/appusers/cashprize";
    public static final String cloesAnchor(String id) {//关闭直播
        return "/api/v1/room/" + id + "/leave";
    }


    public static final String roomLiveValid(String roomId) {
        return "/api/v1/room/" + roomId + "/live/valid";
    }

    /***
     * 接受与主播的连麦请求id
     *
     * @param roomId
     * @return
     */
    public static final String RequestIdlianmai(String roomId) {
        return "/api/v1/room/" + roomId + "/live";
    }

    /**
     * 获取用户申请信息
     * 服务器接口地址：http://miulive.cc:8080/api/v1/room/{roomId}/{userId}/live get请求 其中roomId为直播间id，不是roomid 参数字段，userId为用户id 举例
     *
     *
     * @author sll
     * @time 2016/12/16 11:08
     */
    public static final String roomLive(String roomId, String userId) {

        return "/api/v1/room/" + roomId + "/" + userId + "/live";

    }

    /**
     * 主播30进行一次数据的刷新；
     * http://miulive.cc:8080/api/v1/room/{roomId}/live
     *
     * @author sll
     * @time 2016/12/9 17:44
     */
    public static final String TimerLive(String roomId) {
        return "/api/v1/room/" + roomId + "/alive";
    }

    /**
     * 用戶300秒进行一次数据的更新；
     * http://miulive.cc:8080/api/v1/room/{roomId}/live
     *
     * @author sll
     * @time 2016/12/9 17:44
     */
    public static final String TimerUserLive(String roomId, String userId) {
        return "/api/v1/room/" + roomId + "/user/" + userId + "/alive";
    }


    /**
     * 我的界面图片展示
     *
     * @param id
     * @return
     */
    public static final String myPhoto(String id) {
        return "/api/v1/user/" + id + "/album/pics";
    }


    /**
     * 观众退出连线
     * 服务器接口地址：http://miulive.cc:8080/api/v1/room/{roomId}/{userId}/live/exit get请求 其中roomId为直播间id，不是roomid 参数字段，userId为用户id
     *
     * @author sll
     * @time 2016/12/16 11:08
     */
    public static final String roomLiveExit(String roomId, String userId) {
        return "/api/v1/room/" + roomId + "/" + userId + "/live/exit";
    }

    public static final String photoDelete(String id) {//图片详情删除
        return "/api/v1/user/album/" + id;
    }


    public static final String myattention(String id) {//关注人的直播列表
        return "/api/v1/live/user/" + id + "/follow";
    }


    public static final String myVideo(String id) {//我的界面视频图片展示
        return "/api/v1/user/" + id + "/album/videos";
    }

    public static final String livepush(String id) {//livepush
        return "/api/v1/room/" + id + "/push";
    }


    /**
     * 构建请求url
     *
     * @param route
     * @param params
     * @return
     */
    public String build(String route, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(serverUrl);//.append("/").append(serverName);
        sb.append(route);
        if (params != null && params.size() > 0) {
            int index = 0;
            for (String key : params.keySet()) {
                String mark = index++ == 0 ? "?" : "&";
                String v = params.get(key);
                if (TextUtils.isEmpty(v)) {
                    LogUtils.e("key: " + key);
                }
                try {
                    String value = URLEncoder.encode(params.get(key), "UTF-8");
                    sb.append(mark).append(key).append("=").append(value);
                } catch (UnsupportedEncodingException e) {
                }
            }
        }
        return sb.toString();
    }

    public String buildChaServer(String route, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(chargeServerUrl);//.append("/").append(serverName);
        sb.append(route);
        if (params != null && params.size() > 0) {
            int index = 0;
            for (String key : params.keySet()) {
                String mark = index++ == 0 ? "?" : "&";
                String v = params.get(key);
                if (TextUtils.isEmpty(v)) {
                    LogUtils.e("key: " + key);
                }
                try {
                    String value = URLEncoder.encode(params.get(key), "UTF-8");
                    sb.append(mark).append(key).append("=").append(value);
                } catch (UnsupportedEncodingException e) {
                }
            }
        }
        return sb.toString();
    }

}
