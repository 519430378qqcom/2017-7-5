package com.lvshandian.lemeng.moudles.index.live.bullfight;

/**
 * Created by dong on 2017/5/26.
 */

public interface BullfightInterface {
    /**
     * 返回游戏开启结果
     * @param result
     */
    void startBullGame(StartResult result);

    /**
     * 返回倒计时信息
     * @param timeAndNper
     */
    void getTimeAndNPer(TimeAndNper timeAndNper);

    /**
     * 返回庄家信息
      * @param bankerInfo
     */
    void getBankerInfo(BankerInfo bankerInfo);

    /**
     * 投注成功
     */
    void betSuccess(BetResult betResult,int amount, int type);

    /**
     * 更新庄家余额
     * @param BankerBalance
     */
    void updataBankerBalance(BankerBalance BankerBalance);

    /**
     * 返回扑克牌结果
     * @param pokerResult
     */
    void getPokerResult(PokerResult pokerResult);

    /**
     * 获得游戏结果
     * @param gameResult
     */
    void getGameResult(GameResult gameResult);
}
