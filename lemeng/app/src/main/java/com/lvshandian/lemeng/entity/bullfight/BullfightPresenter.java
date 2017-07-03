package com.lvshandian.lemeng.entity.bullfight;

import com.alibaba.fastjson.JSON;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.net.UrlBuilder;
import com.lvshandian.lemeng.utils.LogUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * author : Cui Dong
 * e-mail : dgsimle@sina.com
 * time   : 2017/5/26
 * version: 1.0
 * desc   : 牛牛游戏的presenter
 */

public class BullfightPresenter {
    private BullfightInterface bullfightInterface;

    public BullfightPresenter(BullfightInterface bullfightInterface) {
        this.bullfightInterface = bullfightInterface;
    }

    public void detach() {
        bullfightInterface = null;
    }

    /**
     * 请求开启斗牛游戏
     *
     * @param roomId
     */
    public void startBullGame(String roomId) {
        String url = UrlBuilder.GAME_BASE + UrlBuilder.BULLFIGHT_START + "?roomId=" + roomId;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                StartResult startResult = new StartResult();
                startResult.setSuccess(false);
                if (bullfightInterface != null) {
                    bullfightInterface.startBullGame(startResult);
                }
            }

            @Override
            public void onResponse(String response) {
                LogUtils.e("TAG", "startBullGame" + response);
                StartResult startResult = null;
                try {
                    startResult = JSON.parseObject(response, StartResult.class);
                } catch (Exception e) {
                    startResult = new StartResult();
                    startResult.setSuccess(false);
                }
                if (bullfightInterface != null) {
                    bullfightInterface.startBullGame(startResult);
                }
            }
        });
    }

    ;

    /**
     * 获取庄家信息
     */
    public void getBankerInfo() {
        String url = UrlBuilder.GAME_BASE + UrlBuilder.BULLFIGHT_BANKER;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtils.e("TAG", "getBankerInfo" + response);
                BankerInfo bankerInfo = null;
                try {
                    bankerInfo = JSON.parseObject(response, BankerInfo.class);
                } catch (Exception e) {
                    bankerInfo = new BankerInfo();
                    bankerInfo.setSuccess(false);
                }
                if (bullfightInterface != null) {
                    bullfightInterface.getBankerInfo(bankerInfo);
                }
            }
        });
    }

    ;

    /**
     * 请求获取斗牛游戏倒计时和期数
     *
     * @param roomId
     */
    public void getTimeAndNper(String roomId) {
        String url = UrlBuilder.GAME_BASE + UrlBuilder.BULLFIGHT_TIMER + "?roomId=" + roomId;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtils.e("TAG", "getTimeAndNper" + response);
                TimeAndNper timeAndNper = null;
                try {
                    timeAndNper = JSON.parseObject(response, TimeAndNper.class);
                } catch (Exception e) {
                    timeAndNper = new TimeAndNper();
                    timeAndNper.setSuccess(false);
                }
                if (bullfightInterface != null) {
                    bullfightInterface.getTimeAndNPer(timeAndNper);
                }
            }
        });
    }

    /**
     * 生成开奖结果（主播第五秒调用）,
     */
    public void initGameResult(String roomId) {
        String url = UrlBuilder.GAME_BASE + UrlBuilder.BULLFIGHT_INITGAME + "?roomId=" + roomId;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtils.e("TAG", "initGameResult" + response);
            }
        });
    }

    /**
     * 获取扑克结果
     */
    public void getPokerResult(String roomId) {
        String url = UrlBuilder.GAME_BASE + UrlBuilder.BULLFIGHT_POKER_RESULT + "?roomId=" + roomId;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtils.e("TAG", "getPokerResult" + response);
                PokerResult pokerResult = null;
                try {
                    pokerResult = JSON.parseObject(response, PokerResult.class);
                    if (pokerResult.getObj() == null) {
                        pokerResult.setSuccess(false);
                    }
                } catch (Exception e) {
                    pokerResult = new PokerResult();
                    pokerResult.setSuccess(false);
                }
                if (bullfightInterface != null) {
                    bullfightInterface.getPokerResult(pokerResult);
                }
            }
        });
    }

    /**
     * 投注
     *
     * @param userId
     * @param roomId
     * @param amount 投注金额
     * @param type   1为投注天2为投注地3为人
     * @param perid  期数
     * @param status 0 为1倍 1为2倍
     */
    public void betSuccess(int userId, int roomId, final int amount, final int type, int perid, int status) {
        String url = UrlBuilder.betting(userId, roomId, amount, type, perid, status);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtils.e("TAG", "betSuccess" + response);
                BetResult betResult = null;
                try {
                    betResult = JSON.parseObject(response, BetResult.class);
                } catch (Exception e) {
                    betResult = new BetResult();
                    betResult.setSuccess(false);
                }
                if (bullfightInterface != null) {
                    bullfightInterface.betSuccess(betResult, amount, type);
                }
            }
        });
    }

    /**
     * 获取游戏结果
     */
    public void getGameResult(String roomId, String perid, String userId) {
        String url = UrlBuilder.GAME_BASE + UrlBuilder.BULLFIGHT_RESULT + "?roomId=" + roomId + "&perid=" + perid + "&userId=" + userId;
        LogUtils.e("TAG", "getGameResult=" + url);
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtils.e("TAG", "getGameResult" + response);
                GameResult gameResult = null;
                try {
                    gameResult = JSON.parseObject(response, GameResult.class);
                } catch (Exception e) {
                    gameResult = new GameResult();
                    gameResult.setSuccess(false);
                }
                if (bullfightInterface != null) {
                    bullfightInterface.getGameResult(gameResult);
                }
            }
        });
    }

    /**
     * 刷新庄家账户
     */
    public void updateBankerBalance() {
        String url = UrlBuilder.GAME_BASE + UrlBuilder.BULLFIGHT_BANKER_BALANCE;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                LogUtils.e("TAG", "updateBankerBalance" + response);
                BankerBalances bankerBalance = null;
                try {
                    bankerBalance = JSON.parseObject(response, BankerBalances.class);
                } catch (Exception e) {
                    bankerBalance = new BankerBalances();
                    bankerBalance.setSuccess(false);
                }
                if (bullfightInterface != null) {
                    bullfightInterface.updataBankerBalance(bankerBalance);
                }
            }
        });
    }

    /**
     * 初始化倒计时
     */
    public void initGameTimer(String roomId, String perId, String userId) {
        String url = UrlBuilder.GAME_BASE + UrlBuilder.BULLFIGHT_INITTIME + "?roomId=" + roomId + "&perid=" + perId + "&userId=" + userId;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
                bullfightInterface.initGameTimer(false);
            }

            @Override
            public void onResponse(String response) {
                LogUtils.e("TAG", "initGameTimer" + response);
                bullfightInterface.initGameTimer(true);
            }
        });
    }
    /**
     * 计算直播间所有牛牛游戏结果
     */
    public void computeAllResult(String roomId, String perId) {
        String url = UrlBuilder.GAME_BASE + UrlBuilder.ALL_RESULT + "?roomId=" + roomId + "&perid=" + perId;
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
            }
        });
    }
    /**
     * 获取牛几的图片id
     *
     * @return
     */
    public int getBullSumId(int sum) {
        switch (sum) {
            case 0:
                return R.mipmap.bull0;
            case 1:
                return R.mipmap.bull1;
            case 2:
                return R.mipmap.bull2;
            case 3:
                return R.mipmap.bull3;
            case 4:
                return R.mipmap.bull4;
            case 5:
                return R.mipmap.bull5;
            case 6:
                return R.mipmap.bull6;
            case 7:
                return R.mipmap.bull7;
            case 8:
                return R.mipmap.bull8;
            case 9:
                return R.mipmap.bull9;
            case 10:
                return R.mipmap.bullbull;
        }
        return 0;
    }

    /**
     * 根据颜色和数值获取图片id
     *
     * @return
     */
    public int getPokerId(int color, int value) {
        switch (color) {
            case 1:
                switch (value) {
                    case 1:
                        return R.mipmap.poker_1_1;
                    case 2:
                        return R.mipmap.poker_1_2;
                    case 3:
                        return R.mipmap.poker_1_3;
                    case 4:
                        return R.mipmap.poker_1_4;
                    case 5:
                        return R.mipmap.poker_1_5;
                    case 6:
                        return R.mipmap.poker_1_6;
                    case 7:
                        return R.mipmap.poker_1_7;
                    case 8:
                        return R.mipmap.poker_1_8;
                    case 9:
                        return R.mipmap.poker_1_9;
                    case 10:
                        return R.mipmap.poker_1_10;
                    case 11:
                        return R.mipmap.poker_1_11;
                    case 12:
                        return R.mipmap.poker_1_12;
                    case 13:
                        return R.mipmap.poker_1_13;
                }
            case 2:
                switch (value) {
                    case 1:
                        return R.mipmap.poker_2_1;
                    case 2:
                        return R.mipmap.poker_2_2;
                    case 3:
                        return R.mipmap.poker_2_3;
                    case 4:
                        return R.mipmap.poker_2_4;
                    case 5:
                        return R.mipmap.poker_2_5;
                    case 6:
                        return R.mipmap.poker_2_6;
                    case 7:
                        return R.mipmap.poker_2_7;
                    case 8:
                        return R.mipmap.poker_2_8;
                    case 9:
                        return R.mipmap.poker_2_9;
                    case 10:
                        return R.mipmap.poker_2_10;
                    case 11:
                        return R.mipmap.poker_2_11;
                    case 12:
                        return R.mipmap.poker_2_12;
                    case 13:
                        return R.mipmap.poker_2_13;
                }
            case 3:
                switch (value) {
                    case 1:
                        return R.mipmap.poker_3_1;
                    case 2:
                        return R.mipmap.poker_3_2;
                    case 3:
                        return R.mipmap.poker_3_3;
                    case 4:
                        return R.mipmap.poker_3_4;
                    case 5:
                        return R.mipmap.poker_3_5;
                    case 6:
                        return R.mipmap.poker_3_6;
                    case 7:
                        return R.mipmap.poker_3_7;
                    case 8:
                        return R.mipmap.poker_3_8;
                    case 9:
                        return R.mipmap.poker_3_9;
                    case 10:
                        return R.mipmap.poker_3_10;
                    case 11:
                        return R.mipmap.poker_3_11;
                    case 12:
                        return R.mipmap.poker_3_12;
                    case 13:
                        return R.mipmap.poker_3_13;
                }
            case 4:
                switch (value) {
                    case 1:
                        return R.mipmap.poker_4_1;
                    case 2:
                        return R.mipmap.poker_4_2;
                    case 3:
                        return R.mipmap.poker_4_3;
                    case 4:
                        return R.mipmap.poker_4_4;
                    case 5:
                        return R.mipmap.poker_4_5;
                    case 6:
                        return R.mipmap.poker_4_6;
                    case 7:
                        return R.mipmap.poker_4_7;
                    case 8:
                        return R.mipmap.poker_4_8;
                    case 9:
                        return R.mipmap.poker_4_9;
                    case 10:
                        return R.mipmap.poker_4_10;
                    case 11:
                        return R.mipmap.poker_4_11;
                    case 12:
                        return R.mipmap.poker_4_12;
                    case 13:
                        return R.mipmap.poker_4_13;
                }
        }
        return 0;
    }
}
