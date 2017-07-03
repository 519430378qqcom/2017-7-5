package com.lvshandian.lemeng.entity;

import java.io.Serializable;

/**
 * Created by zhang on 2017/1/18.
 */

public class LiveListBean implements Serializable {


    /**
     * id : 1000000   主播ID
     * userName : 17600330098
     * password : e10adc3949ba59abbe56e057f20f883e
     * address : 北京市
     * age : null
     * nickName : llllll啊啦啦啦
     * picUrl : http://lemeng-news.oss-cn-beijing.aliyuncs.com/avatar/1000000/objectKey-17600330098-headerpic-1491532351.png
     * signature : 啦啦队
     * gender : 1
     * phoneNum : 17600330098
     * level : 6
     * location : 39.97960,116.47306
     * points : 309062
     * veriInfo : null
     * verified : 0
     * neteaseAccount : miu_1000000
     * neteaseToken : cd18e9e485eda01b095d4ade57b9ce89
     * regTime : 2017-03-20 14:33:43
     * live : 1
     * online : 1
     * refreshTime : 1491882393823
     * status : 1
     * goldCoin : 5671544
     * autoUpdateTime : 2017-04-10 12:10:03
     * livePicUrl : http://lemeng-news.oss-cn-beijing.aliyuncs.com/live/1000000/liveshowpic-1491880985.png
     * vip : 0
     * deviceToken : A3127569-B248-4402-8A88-DD49F19476B6
     * fansNum : 9
     * followNum : 8
     * lockedGoldCoin : 0
     * receivedGoldCoin : 5843544
     * spendGoldCoin : 303200
     * shareCode : C3IHDFJM
     * introducerId : 0
     * gradePleased : 0
     * gradeUnsatisfy : 0
     * gradeSatisfied : 0
     * servicePleased : 0
     * serviceUnsatisfy : 0
     * serviceSatisfied : 0
     * payForVideoChat : 0
     * unionId : null
     * exchangeGoldCoin : 0
     * exchangeCash : 0.0
     * exchangeStatus : 0
     * roomId : 6236
     * robot : 0
     * consortiaId : 0
     * vipTime : null
     * channelId : 0
     * familyId : 1
     * familyLeaderFlag : 0
     * constellation : 未填写
     * realName : null
     * liveTime : 35783812093
     * winType : 0
     * loginStatus : 0
     * fanAddNum : null
     * goldAddNum : null
     * liveAddTime : null
     * identityCode : null
     * ishot : 1
     * followFlag : null
     * follow : null
     * controllFlag : null
     * sumAmount : null
     * niceNum : null
     * channel : null
     * family : null
     * userControl : null
     * rewardDetails : null
     * userContribution : null
     * goodNum : null
     * rooms : {"id":6236,"userId":1000000,"name":"","city":"北京市","roomId":8411693,"livePicUrl":"http://lemeng-news.oss-cn-beijing.aliyuncs.com/live/1000000/liveshowpic-1491880985.png","pubStat":0,"publishUrl":"rtmp://pili-publish.lemenglive.com/lemengzhibo/lemengzhibo1491880988433A?e=1491884588&token=_8EgPhKIHK6v4Y58ZDAGZDmCdwyAF7yOf76RQqUf:WO_e_QXwoQnyhcsA2bdWfTDBiE4=","broadcastUrl":"http://pili-live-hdl.lemenglive.com/lemengzhibo/lemengzhibo1491880988433A.flv","shareUrl":"http://miulive.cc/s/6236","payForChat":0,"privateChat":1,"timeStamp":1491880988601,"onlineUserNum":161,"status":1,"likeNum":0,"hotNum":0,"refreshTime":1491882399788,"privateFlag":null,"roomPw":null,"roomPay":null,"rankNum":0,"rankTime":null,"users":null,"publishUrlTitle":null}
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
    private String location;
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
    private int goldCoin;
    private String autoUpdateTime;
    private String livePicUrl;
    private int vip;
    private String deviceToken;
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
    private int exchangeGoldCoin;
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
    private long liveTime;
    private int winType;
    private String loginStatus;
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
    private Object family;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
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

    public int getGoldCoin() {
        return goldCoin;
    }

    public void setGoldCoin(int goldCoin) {
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

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
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

    public int getExchangeGoldCoin() {
        return exchangeGoldCoin;
    }

    public void setExchangeGoldCoin(int exchangeGoldCoin) {
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

    public long getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(long liveTime) {
        this.liveTime = liveTime;
    }

    public int getWinType() {
        return winType;
    }

    public void setWinType(int winType) {
        this.winType = winType;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
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

    public Object getFamily() {
        return family;
    }

    public void setFamily(Object family) {
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

    public static class RoomsBean implements Serializable{
        /**
         * id : 6236              后台房间ID
         * userId : 1000000      主播的ID
         * name :
         * city : 北京市
         * roomId : 8411693    网易云信ID
         * livePicUrl : http://lemeng-news.oss-cn-beijing.aliyuncs.com/live/1000000/liveshowpic-1491880985.png
         * pubStat : 0
         * publishUrl : rtmp://pili-publish.lemenglive.com/lemengzhibo/lemengzhibo1491880988433A?e=1491884588&token=_8EgPhKIHK6v4Y58ZDAGZDmCdwyAF7yOf76RQqUf:WO_e_QXwoQnyhcsA2bdWfTDBiE4=
         * broadcastUrl : http://pili-live-hdl.lemenglive.com/lemengzhibo/lemengzhibo1491880988433A.flv
         * shareUrl : http://miulive.cc/s/6236
         * payForChat : 0
         * privateChat : 1
         * timeStamp : 1491880988601
         * onlineUserNum : 161
         * status : 1
         * likeNum : 0
         * hotNum : 0
         * refreshTime : 1491882399788
         * privateFlag : null
         * roomPw : null
         * roomPay : null
         * rankNum : 0
         * rankTime : null
         * users : null
         * publishUrlTitle : null
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
        private Object privateFlag;
        private Object roomPw;
        private Object roomPay;
        private int rankNum;
        private Object rankTime;
        private Object users;
        private Object publishUrlTitle;
        private String roomsType;//0为直播,1为游戏

        public String getRoomsType() {
            return roomsType;
        }

        public void setRoomsType(String roomsType) {
            this.roomsType = roomsType;
        }

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

        public Object getPrivateFlag() {
            return privateFlag;
        }

        public void setPrivateFlag(Object privateFlag) {
            this.privateFlag = privateFlag;
        }

        public Object getRoomPw() {
            return roomPw;
        }

        public void setRoomPw(Object roomPw) {
            this.roomPw = roomPw;
        }

        public Object getRoomPay() {
            return roomPay;
        }

        public void setRoomPay(Object roomPay) {
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

        public Object getPublishUrlTitle() {
            return publishUrlTitle;
        }

        public void setPublishUrlTitle(Object publishUrlTitle) {
            this.publishUrlTitle = publishUrlTitle;
        }
    }
}
