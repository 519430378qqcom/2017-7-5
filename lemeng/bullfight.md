###斗牛游戏主要类接口
1.BullfightPresenter 斗牛游戏接口请求<br>
2.BullfightInterface 斗牛游戏接口，用于请求成功后的回调<br>
3.BullfightAudio 斗牛音效的管理类
4.为了方便查找 BullfightPresenter中的方法名和BullfightInterface接口回调名相同并且一一对应
###斗牛游戏流程说明
1.点击开启游戏调用startBullfightGame创建BullfightPresenter类并调用startBullGame
2.startBullGame回调成功后调用getTimeAndNper（获取游戏倒计时和期数）；getBankerInfo（获取庄家信息）<br>
3.getTimeAndNper回调成功后执行myHandler.sendEmptyMessage(BULLFIGHT_TIME);开启牛牛倒计时
4.网络正常的情况下返回的时间值为35，随后执行
<pre>
 /**
      * 斗牛倒计时
      */
     private void bullfightTimer() {
         myHandler.sendEmptyMessageDelayed(BULLFIGHT_TIME, 1000);
         //前20秒执行switchTimer();倒计时动画
         if (nextTime >= 15) {
             switchTimer();
         }
         //后五秒显示休息时间
         if (nextTime <= 5 && nextTime > 0) {
             bullfightResultShow(null, null, getString(R.string.take_a_rest) + nextTime + "S");
         }
         switch (nextTime) {
             case 30://主播初始化游戏结果，调用此接口服务器才会生成4 副牌型
                 bullfightPresenter.initGameResult(room_Id);
                 break;
             case 15://获取扑克牌结果
                 setBetPoolEnable(false);
                 rl_timing.setVisibility(View.GONE);
                 bullfightPresenter.getPokerResult(room_Id);
                 //并告诉服务端计算直播间所有牛牛游戏结果（只有主播端会调用）
                 bullfightPresenter.computeAllResult(room_Id, uper + "");
                 break;
             case 8://开奖
                 bullfightPresenter.getGameResult(room_Id, uper + "", appUser.getId());
                 bullfightPresenter.updateBankerBalance();
                 break;
             case 0://主播更新倒计时
                 myHandler.removeMessages(BULLFIGHT_TIME);
                 bullfightPresenter.initGameTimer(room_Id, uper + "", appUser.getId() + "");
                 break;
         }
         nextTime--;
     }

</pre>
5.最后主播调用初始化下一局游戏initGameTimer成功后执行回调
<pre>
@Override
    public void initGameTimer(Boolean isSuccess) {
        if (isSuccess) {
            //重新获取下一局的倒计时和期数
            bullfightPresenter.getTimeAndNper(room_Id);
            //主播端发送type为3131的消息，观众端接收到后才去请求下一局的数据，保持时间数据同步，
            Map<String, Object> map = new HashMap<>();
            map.put("vip", appUser.getVip());
            map.put("userId", appUser.getId());
            map.put("level", appUser.getLevel());
            map.put("NIM_CONTER_TIMER_MPER", uper + "");
            SendRoomMessageUtils.onCustomMessagePlay("3131", messageFragment, wy_Id, map);
        } else {//尝试重连
            bullfightPresenter.initGameTimer(room_Id, uper + "", appUser.getId() + "");
        }
    }
    </pre>
6.斗牛游戏接口不规范:
①请求传参时，参数名userId roomId I 为大写，而perid的i为小写<br>
②还有同名的参数userId和roomId开心时为int类型，不开心时是string类型。
