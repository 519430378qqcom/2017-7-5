package com.lvshandian.lemeng.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 */

public class LiveFamilyMemberBean implements Serializable {

    /**
     * success : true
     * code : 1
     * msg : 查询成功
     * obj : [{"id":1000005,"userName":"15200124042","password":"e10adc3949ba59abbe56e057f20f883e","address":"邯郸","age":null,"nickName":"悦琴","picUrl":"http://lemeng-news.oss-cn-beijing.aliyuncs.com/11765.png","signature":"海边散步的大虾","gender":"0","phoneNum":"15200124042","level":6,"location":null,"points":379054,"veriInfo":null,"verified":0,"neteaseAccount":"miu_1000005","neteaseToken":"eedfde7410fb8b6abbe217a497d5b779","regTime":"2017-03-10 16:14:10","live":1,"online":0,"refreshTime":1489147665155,"status":1,"goldCoin":718072,"autoUpdateTime":"2017-03-10 20:18:00","livePicUrl":"http://lemeng-news.oss-cn-beijing.aliyuncs.com/65969.png","vip":0,"deviceToken":null,"fansNum":2,"followNum":1,"lockedGoldCoin":0,"receivedGoldCoin":9912,"spendGoldCoin":379040,"shareCode":"DB7YVRX9","introducerId":0,"gradePleased":0,"gradeUnsatisfy":0,"gradeSatisfied":0,"servicePleased":0,"serviceUnsatisfy":0,"serviceSatisfied":0,"payForVideoChat":0,"unionId":null,"exchangeGoldCoin":null,"exchangeCash":0,"exchangeStatus":0,"roomId":4831,"robot":0,"consortiaId":0,"vipTime":null,"channelId":0,"familyId":1,"familyLeaderFlag":0,"constellation":"摩羯座","realName":null,"liveTime":5181241,"winType":0,"fanAddNum":null,"goldAddNum":null,"liveAddTime":null,"identityCode":null,"ishot":0,"followFlag":null,"follow":null,"controllFlag":null,"sumAmount":null,"niceNum":null,"channel":null,"family":{"familyId":null,"familyName":"才艺家族","familyDesc":null,"createtime":null,"updatetime":null,"createUserId":null,"updateUserId":null},"userControl":null,"rewardDetails":null,"userContribution":null,"goodNum":null,"rooms":{"id":4831,"userId":1000005,"name":"悦琴","city":"火星","roomId":7778402,"livePicUrl":"http://lemeng-news.oss-cn-beijing.aliyuncs.com/65969.png","pubStat":0,"publishUrl":"rtmp://pili-publish.tangrenlive.com/tangrenlive/tangrenlive1489370665702A?e=1489374265&token=WigBgDzxiI8k6GYTpXE2D-EeTWmojJXcvinHb9hj:vJWg-SStqPZye2lqBExy0Bs0DmI=","broadcastUrl":"http://pili-live-hdl.tangrenlive.com/tangrenlive/tangrenlive1489370665702A.flv","shareUrl":"http://miulive.cc/s/4831","payForChat":0,"privateChat":1,"timeStamp":1489370665863,"onlineUserNum":6,"status":1,"likeNum":0,"hotNum":0,"refreshTime":1489370697012,"privateFlag":null,"roomPw":null,"roomPay":null,"rankNum":0,"rankTime":null,"users":null},"idno":null,"idfrontPic":null,"idbackPic":null}]
     */

    private boolean success;
    private int code;
    private String msg;
    private List<ObjBean> obj;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * id : 1000005
         * userName : 15200124042
         * password : e10adc3949ba59abbe56e057f20f883e
         * address : 邯郸
         * age : null
         * nickName : 悦琴
         * picUrl : http://lemeng-news.oss-cn-beijing.aliyuncs.com/11765.png
         * signature : 海边散步的大虾
         * gender : 0
         * phoneNum : 15200124042
         * level : 6
         * location : null
         * points : 379054
         * veriInfo : null
         * verified : 0
         * neteaseAccount : miu_1000005
         * neteaseToken : eedfde7410fb8b6abbe217a497d5b779
         * regTime : 2017-03-10 16:14:10
         * live : 1
         * online : 0
         * refreshTime : 1489147665155
         * status : 1
         * goldCoin : 718072
         * autoUpdateTime : 2017-03-10 20:18:00
         * livePicUrl : http://lemeng-news.oss-cn-beijing.aliyuncs.com/65969.png
         * vip : 0
         * deviceToken : null
         * fansNum : 2
         * followNum : 1
         * lockedGoldCoin : 0
         * receivedGoldCoin : 9912
         * spendGoldCoin : 379040
         * shareCode : DB7YVRX9
         * introducerId : 0
         * gradePleased : 0
         * gradeUnsatisfy : 0
         * gradeSatisfied : 0
         * servicePleased : 0
         * serviceUnsatisfy : 0
         * serviceSatisfied : 0
         * payForVideoChat : 0
         * unionId : null
         * exchangeGoldCoin : null
         * exchangeCash : 0.0
         * exchangeStatus : 0
         * roomId : 4831
         * robot : 0
         * consortiaId : 0
         * vipTime : null
         * channelId : 0
         * familyId : 1
         * familyLeaderFlag : 0
         * constellation : 摩羯座
         * realName : null
         * liveTime : 5181241
         * winType : 0
         * fanAddNum : null
         * goldAddNum : null
         * liveAddTime : null
         * identityCode : null
         * ishot : 0
         * followFlag : null
         * follow : null
         * controllFlag : null
         * sumAmount : null
         * niceNum : null
         * channel : null
         * family : {"familyId":null,"familyName":"才艺家族","familyDesc":null,"createtime":null,"updatetime":null,"createUserId":null,"updateUserId":null}
         * userControl : null
         * rewardDetails : null
         * userContribution : null
         * goodNum : null
         * rooms : {"id":4831,"userId":1000005,"name":"悦琴","city":"火星","roomId":7778402,"livePicUrl":"http://lemeng-news.oss-cn-beijing.aliyuncs.com/65969.png","pubStat":0,"publishUrl":"rtmp://pili-publish.tangrenlive.com/tangrenlive/tangrenlive1489370665702A?e=1489374265&token=WigBgDzxiI8k6GYTpXE2D-EeTWmojJXcvinHb9hj:vJWg-SStqPZye2lqBExy0Bs0DmI=","broadcastUrl":"http://pili-live-hdl.tangrenlive.com/tangrenlive/tangrenlive1489370665702A.flv","shareUrl":"http://miulive.cc/s/4831","payForChat":0,"privateChat":1,"timeStamp":1489370665863,"onlineUserNum":6,"status":1,"likeNum":0,"hotNum":0,"refreshTime":1489370697012,"privateFlag":null,"roomPw":null,"roomPay":null,"rankNum":0,"rankTime":null,"users":null}
         * idno : null
         * idfrontPic : null
         * idbackPic : null
         */

        private int id;
        private String userName;
        private String password;
        private String address;
        private Object age;
        private String nickName;
        private String picUrl;
        private String signature;
        private String gender;
        private String phoneNum;
        private int level;
        private Object location;
        private int points;
        private Object veriInfo;
        private int verified;
        private String neteaseAccount;
        private String neteaseToken;
        private String regTime;
        private int live;
        private int online;
        private long refreshTime;
        private int status;
        private long goldCoin;
        private String autoUpdateTime;
        private String livePicUrl;
        private int vip;
        private Object deviceToken;
        private int fansNum;
        private int followNum;
        private int lockedGoldCoin;
        private int receivedGoldCoin;
        private int spendGoldCoin;
        private String shareCode;
        private int introducerId;
        private int gradePleased;
        private int gradeUnsatisfy;
        private int gradeSatisfied;
        private int servicePleased;
        private int serviceUnsatisfy;
        private int serviceSatisfied;
        private int payForVideoChat;
        private Object unionId;
        private Object exchangeGoldCoin;
        private double exchangeCash;
        private int exchangeStatus;
        private int roomId;
        private int robot;
        private int consortiaId;
        private Object vipTime;
        private int channelId;
        private int familyId;
        private int familyLeaderFlag;
        private String constellation;
        private Object realName;
        private int liveTime;
        private int winType;
        private Object fanAddNum;
        private Object goldAddNum;
        private Object liveAddTime;
        private Object identityCode;
        private int ishot;
        private Object followFlag;
        private Object follow;
        private Object controllFlag;
        private Object sumAmount;
        private Object niceNum;
        private Object channel;
        private FamilyBean family;
        private Object userControl;
        private Object rewardDetails;
        private Object userContribution;
        private Object goodNum;
        private RoomsBean rooms;
        private Object idno;
        private Object idfrontPic;
        private Object idbackPic;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getAge() {
            return age;
        }

        public void setAge(Object age) {
            this.age = age;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getPicUrl() {
            return picUrl;
        }

        public void setPicUrl(String picUrl) {
            this.picUrl = picUrl;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public Object getLocation() {
            return location;
        }

        public void setLocation(Object location) {
            this.location = location;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public Object getVeriInfo() {
            return veriInfo;
        }

        public void setVeriInfo(Object veriInfo) {
            this.veriInfo = veriInfo;
        }

        public int getVerified() {
            return verified;
        }

        public void setVerified(int verified) {
            this.verified = verified;
        }

        public String getNeteaseAccount() {
            return neteaseAccount;
        }

        public void setNeteaseAccount(String neteaseAccount) {
            this.neteaseAccount = neteaseAccount;
        }

        public String getNeteaseToken() {
            return neteaseToken;
        }

        public void setNeteaseToken(String neteaseToken) {
            this.neteaseToken = neteaseToken;
        }

        public String getRegTime() {
            return regTime;
        }

        public void setRegTime(String regTime) {
            this.regTime = regTime;
        }

        public int getLive() {
            return live;
        }

        public void setLive(int live) {
            this.live = live;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }

        public long getRefreshTime() {
            return refreshTime;
        }

        public void setRefreshTime(long refreshTime) {
            this.refreshTime = refreshTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getGoldCoin() {
            return goldCoin;
        }

        public void setGoldCoin(long goldCoin) {
            this.goldCoin = goldCoin;
        }

        public String getAutoUpdateTime() {
            return autoUpdateTime;
        }

        public void setAutoUpdateTime(String autoUpdateTime) {
            this.autoUpdateTime = autoUpdateTime;
        }

        public String getLivePicUrl() {
            return livePicUrl;
        }

        public void setLivePicUrl(String livePicUrl) {
            this.livePicUrl = livePicUrl;
        }

        public int getVip() {
            return vip;
        }

        public void setVip(int vip) {
            this.vip = vip;
        }

        public Object getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(Object deviceToken) {
            this.deviceToken = deviceToken;
        }

        public int getFansNum() {
            return fansNum;
        }

        public void setFansNum(int fansNum) {
            this.fansNum = fansNum;
        }

        public int getFollowNum() {
            return followNum;
        }

        public void setFollowNum(int followNum) {
            this.followNum = followNum;
        }

        public int getLockedGoldCoin() {
            return lockedGoldCoin;
        }

        public void setLockedGoldCoin(int lockedGoldCoin) {
            this.lockedGoldCoin = lockedGoldCoin;
        }

        public int getReceivedGoldCoin() {
            return receivedGoldCoin;
        }

        public void setReceivedGoldCoin(int receivedGoldCoin) {
            this.receivedGoldCoin = receivedGoldCoin;
        }

        public int getSpendGoldCoin() {
            return spendGoldCoin;
        }

        public void setSpendGoldCoin(int spendGoldCoin) {
            this.spendGoldCoin = spendGoldCoin;
        }

        public String getShareCode() {
            return shareCode;
        }

        public void setShareCode(String shareCode) {
            this.shareCode = shareCode;
        }

        public int getIntroducerId() {
            return introducerId;
        }

        public void setIntroducerId(int introducerId) {
            this.introducerId = introducerId;
        }

        public int getGradePleased() {
            return gradePleased;
        }

        public void setGradePleased(int gradePleased) {
            this.gradePleased = gradePleased;
        }

        public int getGradeUnsatisfy() {
            return gradeUnsatisfy;
        }

        public void setGradeUnsatisfy(int gradeUnsatisfy) {
            this.gradeUnsatisfy = gradeUnsatisfy;
        }

        public int getGradeSatisfied() {
            return gradeSatisfied;
        }

        public void setGradeSatisfied(int gradeSatisfied) {
            this.gradeSatisfied = gradeSatisfied;
        }

        public int getServicePleased() {
            return servicePleased;
        }

        public void setServicePleased(int servicePleased) {
            this.servicePleased = servicePleased;
        }

        public int getServiceUnsatisfy() {
            return serviceUnsatisfy;
        }

        public void setServiceUnsatisfy(int serviceUnsatisfy) {
            this.serviceUnsatisfy = serviceUnsatisfy;
        }

        public int getServiceSatisfied() {
            return serviceSatisfied;
        }

        public void setServiceSatisfied(int serviceSatisfied) {
            this.serviceSatisfied = serviceSatisfied;
        }

        public int getPayForVideoChat() {
            return payForVideoChat;
        }

        public void setPayForVideoChat(int payForVideoChat) {
            this.payForVideoChat = payForVideoChat;
        }

        public Object getUnionId() {
            return unionId;
        }

        public void setUnionId(Object unionId) {
            this.unionId = unionId;
        }

        public Object getExchangeGoldCoin() {
            return exchangeGoldCoin;
        }

        public void setExchangeGoldCoin(Object exchangeGoldCoin) {
            this.exchangeGoldCoin = exchangeGoldCoin;
        }

        public double getExchangeCash() {
            return exchangeCash;
        }

        public void setExchangeCash(double exchangeCash) {
            this.exchangeCash = exchangeCash;
        }

        public int getExchangeStatus() {
            return exchangeStatus;
        }

        public void setExchangeStatus(int exchangeStatus) {
            this.exchangeStatus = exchangeStatus;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public int getRobot() {
            return robot;
        }

        public void setRobot(int robot) {
            this.robot = robot;
        }

        public int getConsortiaId() {
            return consortiaId;
        }

        public void setConsortiaId(int consortiaId) {
            this.consortiaId = consortiaId;
        }

        public Object getVipTime() {
            return vipTime;
        }

        public void setVipTime(Object vipTime) {
            this.vipTime = vipTime;
        }

        public int getChannelId() {
            return channelId;
        }

        public void setChannelId(int channelId) {
            this.channelId = channelId;
        }

        public int getFamilyId() {
            return familyId;
        }

        public void setFamilyId(int familyId) {
            this.familyId = familyId;
        }

        public int getFamilyLeaderFlag() {
            return familyLeaderFlag;
        }

        public void setFamilyLeaderFlag(int familyLeaderFlag) {
            this.familyLeaderFlag = familyLeaderFlag;
        }

        public String getConstellation() {
            return constellation;
        }

        public void setConstellation(String constellation) {
            this.constellation = constellation;
        }

        public Object getRealName() {
            return realName;
        }

        public void setRealName(Object realName) {
            this.realName = realName;
        }

        public int getLiveTime() {
            return liveTime;
        }

        public void setLiveTime(int liveTime) {
            this.liveTime = liveTime;
        }

        public int getWinType() {
            return winType;
        }

        public void setWinType(int winType) {
            this.winType = winType;
        }

        public Object getFanAddNum() {
            return fanAddNum;
        }

        public void setFanAddNum(Object fanAddNum) {
            this.fanAddNum = fanAddNum;
        }

        public Object getGoldAddNum() {
            return goldAddNum;
        }

        public void setGoldAddNum(Object goldAddNum) {
            this.goldAddNum = goldAddNum;
        }

        public Object getLiveAddTime() {
            return liveAddTime;
        }

        public void setLiveAddTime(Object liveAddTime) {
            this.liveAddTime = liveAddTime;
        }

        public Object getIdentityCode() {
            return identityCode;
        }

        public void setIdentityCode(Object identityCode) {
            this.identityCode = identityCode;
        }

        public int getIshot() {
            return ishot;
        }

        public void setIshot(int ishot) {
            this.ishot = ishot;
        }

        public Object getFollowFlag() {
            return followFlag;
        }

        public void setFollowFlag(Object followFlag) {
            this.followFlag = followFlag;
        }

        public Object getFollow() {
            return follow;
        }

        public void setFollow(Object follow) {
            this.follow = follow;
        }

        public Object getControllFlag() {
            return controllFlag;
        }

        public void setControllFlag(Object controllFlag) {
            this.controllFlag = controllFlag;
        }

        public Object getSumAmount() {
            return sumAmount;
        }

        public void setSumAmount(Object sumAmount) {
            this.sumAmount = sumAmount;
        }

        public Object getNiceNum() {
            return niceNum;
        }

        public void setNiceNum(Object niceNum) {
            this.niceNum = niceNum;
        }

        public Object getChannel() {
            return channel;
        }

        public void setChannel(Object channel) {
            this.channel = channel;
        }

        public FamilyBean getFamily() {
            return family;
        }

        public void setFamily(FamilyBean family) {
            this.family = family;
        }

        public Object getUserControl() {
            return userControl;
        }

        public void setUserControl(Object userControl) {
            this.userControl = userControl;
        }

        public Object getRewardDetails() {
            return rewardDetails;
        }

        public void setRewardDetails(Object rewardDetails) {
            this.rewardDetails = rewardDetails;
        }

        public Object getUserContribution() {
            return userContribution;
        }

        public void setUserContribution(Object userContribution) {
            this.userContribution = userContribution;
        }

        public Object getGoodNum() {
            return goodNum;
        }

        public void setGoodNum(Object goodNum) {
            this.goodNum = goodNum;
        }

        public RoomsBean getRooms() {
            return rooms;
        }

        public void setRooms(RoomsBean rooms) {
            this.rooms = rooms;
        }

        public Object getIdno() {
            return idno;
        }

        public void setIdno(Object idno) {
            this.idno = idno;
        }

        public Object getIdfrontPic() {
            return idfrontPic;
        }

        public void setIdfrontPic(Object idfrontPic) {
            this.idfrontPic = idfrontPic;
        }

        public Object getIdbackPic() {
            return idbackPic;
        }

        public void setIdbackPic(Object idbackPic) {
            this.idbackPic = idbackPic;
        }

        public static class FamilyBean {
            /**
             * familyId : null
             * familyName : 才艺家族
             * familyDesc : null
             * createtime : null
             * updatetime : null
             * createUserId : null
             * updateUserId : null
             */

            private Object familyId;
            private String familyName;
            private Object familyDesc;
            private Object createtime;
            private Object updatetime;
            private Object createUserId;
            private Object updateUserId;

            public Object getFamilyId() {
                return familyId;
            }

            public void setFamilyId(Object familyId) {
                this.familyId = familyId;
            }

            public String getFamilyName() {
                return familyName;
            }

            public void setFamilyName(String familyName) {
                this.familyName = familyName;
            }

            public Object getFamilyDesc() {
                return familyDesc;
            }

            public void setFamilyDesc(Object familyDesc) {
                this.familyDesc = familyDesc;
            }

            public Object getCreatetime() {
                return createtime;
            }

            public void setCreatetime(Object createtime) {
                this.createtime = createtime;
            }

            public Object getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(Object updatetime) {
                this.updatetime = updatetime;
            }

            public Object getCreateUserId() {
                return createUserId;
            }

            public void setCreateUserId(Object createUserId) {
                this.createUserId = createUserId;
            }

            public Object getUpdateUserId() {
                return updateUserId;
            }

            public void setUpdateUserId(Object updateUserId) {
                this.updateUserId = updateUserId;
            }
        }

        public static class RoomsBean {
            /**
             * id : 4831
             * userId : 1000005
             * name : 悦琴
             * city : 火星
             * roomId : 7778402
             * livePicUrl : http://lemeng-news.oss-cn-beijing.aliyuncs.com/65969.png
             * pubStat : 0
             * publishUrl : rtmp://pili-publish.tangrenlive.com/tangrenlive/tangrenlive1489370665702A?e=1489374265&token=WigBgDzxiI8k6GYTpXE2D-EeTWmojJXcvinHb9hj:vJWg-SStqPZye2lqBExy0Bs0DmI=
             * broadcastUrl : http://pili-live-hdl.tangrenlive.com/tangrenlive/tangrenlive1489370665702A.flv
             * shareUrl : http://miulive.cc/s/4831
             * payForChat : 0
             * privateChat : 1
             * timeStamp : 1489370665863
             * onlineUserNum : 6
             * status : 1
             * likeNum : 0
             * hotNum : 0
             * refreshTime : 1489370697012
             * privateFlag : null
             * roomPw : null
             * roomPay : null
             * rankNum : 0
             * rankTime : null
             * users : null
             */

            private int id;
            private int userId;
            private String name;
            private String city;
            private int roomId;
            private String livePicUrl;
            private int pubStat;
            private String publishUrl;
            private String broadcastUrl;
            private String shareUrl;
            private int payForChat;
            private int privateChat;
            private long timeStamp;
            private int onlineUserNum;
            private int status;
            private int likeNum;
            private int hotNum;
            private long refreshTime;
            private String privateFlag;
            private String roomPw;
            private String roomPay;
            private int rankNum;
            private Object rankTime;
            private Object users;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public int getRoomId() {
                return roomId;
            }

            public void setRoomId(int roomId) {
                this.roomId = roomId;
            }

            public String getLivePicUrl() {
                return livePicUrl;
            }

            public void setLivePicUrl(String livePicUrl) {
                this.livePicUrl = livePicUrl;
            }

            public int getPubStat() {
                return pubStat;
            }

            public void setPubStat(int pubStat) {
                this.pubStat = pubStat;
            }

            public String getPublishUrl() {
                return publishUrl;
            }

            public void setPublishUrl(String publishUrl) {
                this.publishUrl = publishUrl;
            }

            public String getBroadcastUrl() {
                return broadcastUrl;
            }

            public void setBroadcastUrl(String broadcastUrl) {
                this.broadcastUrl = broadcastUrl;
            }

            public String getShareUrl() {
                return shareUrl;
            }

            public void setShareUrl(String shareUrl) {
                this.shareUrl = shareUrl;
            }

            public int getPayForChat() {
                return payForChat;
            }

            public void setPayForChat(int payForChat) {
                this.payForChat = payForChat;
            }

            public int getPrivateChat() {
                return privateChat;
            }

            public void setPrivateChat(int privateChat) {
                this.privateChat = privateChat;
            }

            public long getTimeStamp() {
                return timeStamp;
            }

            public void setTimeStamp(long timeStamp) {
                this.timeStamp = timeStamp;
            }

            public int getOnlineUserNum() {
                return onlineUserNum;
            }

            public void setOnlineUserNum(int onlineUserNum) {
                this.onlineUserNum = onlineUserNum;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getLikeNum() {
                return likeNum;
            }

            public void setLikeNum(int likeNum) {
                this.likeNum = likeNum;
            }

            public int getHotNum() {
                return hotNum;
            }

            public void setHotNum(int hotNum) {
                this.hotNum = hotNum;
            }

            public long getRefreshTime() {
                return refreshTime;
            }

            public void setRefreshTime(long refreshTime) {
                this.refreshTime = refreshTime;
            }

            public String getPrivateFlag() {
                return privateFlag;
            }

            public void setPrivateFlag(String privateFlag) {
                this.privateFlag = privateFlag;
            }

            public String getRoomPw() {
                return roomPw;
            }

            public void setRoomPw(String roomPw) {
                this.roomPw = roomPw;
            }

            public String getRoomPay() {
                return roomPay;
            }

            public void setRoomPay(String roomPay) {
                this.roomPay = roomPay;
            }

            public int getRankNum() {
                return rankNum;
            }

            public void setRankNum(int rankNum) {
                this.rankNum = rankNum;
            }

            public Object getRankTime() {
                return rankTime;
            }

            public void setRankTime(Object rankTime) {
                this.rankTime = rankTime;
            }

            public Object getUsers() {
                return users;
            }

            public void setUsers(Object users) {
                this.users = users;
            }
        }
    }
}
