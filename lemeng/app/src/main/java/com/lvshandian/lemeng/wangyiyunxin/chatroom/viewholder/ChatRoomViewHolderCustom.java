package com.lvshandian.lemeng.wangyiyunxin.chatroom.viewholder;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.bean.CustomGiftBean;
import com.lvshandian.lemeng.bean.CustomdateBean;
import com.lvshandian.lemeng.utils.GrademipmapUtils;
import com.lvshandian.lemeng.utils.JavaBeanMapUtils;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.lvshandian.lemeng.utils.TextUtils;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.session.emoji.MoonUtil;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderBase;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by sll on 2016/11/22.
 */

/**
 * 直播室聊天的ViewHolder，用于填充直播室聊天数据，根据扩展消息的type不同展示不同数据
 *
 * @author sll
 * @time 2016/11/22 17:07
 */
public class ChatRoomViewHolderCustom extends MsgViewHolderBase {

    private final String GET_DATA_TEXT = "data";
    @Override
    protected boolean isChatRoom() {
        return true;
    }

    @Override
    protected boolean isShowBubble() {
        return false;
    }

    @Override
    protected boolean isShowHeadImage() {
        return true;
    }

    @Override
    public void setNameTextView() {
        nameContainer.setPadding(ScreenUtil.dip2px(6), 0, 0, 0);
        ChatRoomViewHolderHelper.setNameTextView((ChatRoomMessage) message, nameTextView,
                nameIconView, context);
        nameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick();
            }
        });
        nameIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick();
            }
        });

    }

    @Override
    protected int getContentResId() {
        return com.netease.nim.uikit.R.layout.nim_message_item_text_join;
    }

    @Override
    protected void inflateContentView() {

    }

    @Override
    protected void bindContentView() {
        nameTextView.setVisibility(View.GONE);
        avatarLeft.setVisibility(View.INVISIBLE);
        ViewGroup.LayoutParams layoutParams = avatarLeft.getLayoutParams();
        Drawable drawable = context.getResources().getDrawable(R.mipmap.rank_1);
        layoutParams.height = drawable.getMinimumHeight();
//        layoutParams.width = drawable.getIntrinsicWidth();
        avatarLeft.setLayoutParams(layoutParams);
        levelLeft.setLayoutParams(layoutParams);
        levelLeft.setVisibility(View.VISIBLE);
        levelLeft.setImageResource(R.mipmap.rank_6);
        contentContainer.setMinimumHeight(drawable.getIntrinsicHeight());
        TextView bodyTextView = findViewById(com.netease.nim.uikit.R.id
                .nim_message_item_text_body_join);
        bodyTextView.setShadowLayer(1, 0, 2, Color.parseColor("#80000000"));

        TextView bodyLightView = findViewById(com.netease.nim.uikit.R.id
                .nim_message_item_light_body);

        LogUtil.e("网易云信发的信息", message.getRemoteExtension().toString());

        if (message.getRemoteExtension() != null) {
            Map<String, Object> remote0 = message.getRemoteExtension();
            CustomdateBean customdate = null;
            if (((String) remote0.get("type")).equals("107") || ((String) remote0.get("type"))
                    .equals("109") || ((String) remote0.get("type")).equals("114") ||
                    ((String) remote0.get("type")).equals("112") || ((String) remote0.get("type")).equals("113")
                    || ((String) remote0.get("type")).equals("199") || ((String) remote0.get("type")).equals("2828")
                    || ((String) remote0.get("type")).equals("1818") || ((String) remote0.get("type")).equals("2929")) {
                customdate = JavaBeanMapUtils.mapToBean((Map) message.getRemoteExtension(),
                        CustomdateBean.class);
            } else {
                customdate = JavaBeanMapUtils.mapToBean((Map) remote0.get(GET_DATA_TEXT), CustomdateBean
                        .class);
            }
            int level = Integer.valueOf(customdate.getLevel());
            levelLeft.setImageResource(GrademipmapUtils.LevelImg[Integer.valueOf(level) - 1]);
        }

        String name = null;
        if (message.getFromNick() != null) {
            name = message.getFromNick() + ":";
        } else if (((ChatRoomMessage) message).getChatRoomMessageExtension().getSenderNick() !=
                null) {
            name = ((ChatRoomMessage) message).getChatRoomMessageExtension().getSenderNick() + ":";
        } else if (message.getFromAccount() != null) {
            name = message.getFromAccount().substring(4, message.getFromAccount().length()) + ":";
        }

        bodyTextView.setGravity(Gravity.CENTER_VERTICAL);
        bodyTextView.setTextColor(Color.WHITE);
//        bodyTextView.setPadding(ScreenUtil.dip2px(6), 0, 0, 0);
        bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        bodyTextView.setOnLongClickListener(longClickListener);
        bodyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick();
            }
        });
        bodyTextView.setVisibility(View.VISIBLE);
        bodyLightView.setVisibility(View.GONE);
        messageItemLayout.setVisibility(View.VISIBLE);
        messageItemLayout.setPadding(ScreenUtil.dip2px(6), ScreenUtil.dip2px(2), 0, ScreenUtil.dip2px(2));
        String text = "";
        bodyTextView.setTextColor(context.getResources().getColor(R.color.message));
        if (message.getRemoteExtension() != null) {
            Map<String, Object> remote = message.getRemoteExtension();
            String typeText = (String) remote.get("type");
            LogUtils.i("WangYi", "type:" + typeText + "\nremoteExtension:" + remote);
            if (!TextUtils.isEmpty(typeText)) {
                int type = Integer.parseInt(typeText);
                /**  101//创建表演
                 102 //观看表演
                 103//开始表演
                 104 //结束自己创建表演
                 105 //进入房间
                 106 //离开房间
                 107 //点亮
                 108 //点赞.......................................
                 109 //用户打赏
                 110 //观众开始连线
                 111 //观众断开连线
                 112 //主播恢复前台拍摄
                 113 //主播切换到后台
                 114 //禁言
                 199 //关注
                 2828 //主播开游戏
                 1818 //观众投注
                 */
                switch (type) {
                    case 102:
                        //愿意支付你的表演，要加油哦！
                        text = "愿意支付你的表演，要加油哦！";
                        break;
                    case 103:
                        //主播开始表演
                        //主播开始表演了，请支付5，没支付的看不到哦！
                        //{type=103, data={"show_id":"99","cost":"5"}}
                        text = ((ChatRoomMessage) message).getChatRoomMessageExtension()
                                .getSenderNick() + "开始表演了，请支付，没支付的看不到哦！";
                        try {
                            JSONObject jsonObject = new JSONObject((String) remote.get(GET_DATA_TEXT));
                            text = ((ChatRoomMessage) message).getChatRoomMessageExtension()
                                    .getSenderNick() + "开始表演了，请支付" + jsonObject.get("cost") +
                                    "，没支付的看不到哦！";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 104:
                        //主播结束自己创建表演
                        //主播表演结束了，谢谢大家支持！
                        text = "主播表演结束了，谢谢大家支持！";
                        break;
                    case 105:
                        String ZhuBoId = (String) SharedPreferenceUtils.get(context, "ZhuBoId", "");
                        Map map = (Map) remote.get(GET_DATA_TEXT);
                        CustomdateBean customdate = null;
                        if (map != null && map.size() > 0) {
                            LogUtils.i("WangYi", "map.size:" + map.size());
                            customdate = JavaBeanMapUtils.mapToBean((Map) remote.get(GET_DATA_TEXT),
                                    CustomdateBean.class);
                        }
                        LogUtils.i("WangYi", "ZhuBoId:" + ZhuBoId);
                        //进入房间
                        String id = customdate.getUserId();
                        if (TextUtils.isEmpty(id)) {
                            id = customdate.getId();
                        }
                        if (customdate != null && id != null && id.equals(ZhuBoId)) {
                            //如果是主播 "创建房间成功";
                            text = "创建房间成功";
                        } else {
                            //"进入房间";
                            text = "进入房间";
                        }
                        break;
//                    case 106:
//                        //离开房间
//                        // = "主播" + data.get("roomNickname") + "结束直播";
//                        //return ChatRoomViewHolderCustom.class;
//                        break;
                    case 107:
                        //点亮
                        text = "点亮了";
                        bodyLightView.setVisibility(View.VISIBLE);
                        break;
                    case 109:
                        bodyTextView.setTextSize(15);
                        CustomGiftBean customGiftBean = JavaBeanMapUtils.mapToBean(remote,
                                CustomGiftBean.class);
                        if (customGiftBean != null) {
                            LogUtils.i("WangYi", "customGiftBean != null");
                            text = (String) remote.get("gift_item_message");
//                            text=customGiftBean.getGift_item_message();
                        } else {
                            LogUtils.i("WangYi", "customGiftBean == null");
                        }
                        break;
                    case 112:
                        //主播恢复前台拍摄
                        text = "主播回来啦, 视频即将恢复";
                        break;
                    case 113:
                        //主播切换到后台
                        text = "主播离开一下, 精彩不中断, 不要走开哦";
                        break;
                    case 199:
                        //关注
                        text = "关注了主播";
                        break;
                    case 200:
                        //关于点播的本地消息
                        text = (String) remote.get("text");
                        break;
                    case 114://禁言消息
                        text = (String) remote.get("jinyan");
                        break;
                    case 2828://主播开游戏
                        text = (String) remote.get("palyMsg");
                        break;
                    case 1818://有人投注
                        text = (String) remote.get("inputMsg");
                        break;
                    case 2929://主播开斗牛游戏
                        text = (String) remote.get("NIM_BEGIN_GAME_NIUNIU");
                        break;
                    case 3030://有人投注
//                        if(remote.get("NIM_TOUZHU_GOLD_SELECT_1")!=null) {
//                            text = context.getString(R.string.betting1) + remote.get("NIM_TOUZHU_GOLD_SELECT_1");
//                        }else if(remote.get("NIM_TOUZHU_GOLD_SELECT_2")!=null) {
//                            text = context.getString(R.string.betting2) + remote.get("NIM_TOUZHU_GOLD_SELECT_2");
//                        }else if(remote.get("NIM_TOUZHU_GOLD_SELECT_3")!= null) {
//                            text = context.getString(R.string.betting3) + remote.get("NIM_TOUZHU_GOLD_SELECT_3");
//                        }
                        break;
                    default:
                        //默认不显示任何东西
//                        messageItemLayout.setPadding(0, 0, 0, 0);
                        bodyTextView.setVisibility(View.GONE);
                        messageItemLayout.setVisibility(View.GONE);
//                        return ChatRoomMsgViewHolderNotification.class;
                        break;

                }
            }
        }
            MoonUtil.identifyFaceExpression(NimUIKit.getContext(), bodyTextView, name, text,
                    ImageSpan.ALIGN_BOTTOM);
    }

}
