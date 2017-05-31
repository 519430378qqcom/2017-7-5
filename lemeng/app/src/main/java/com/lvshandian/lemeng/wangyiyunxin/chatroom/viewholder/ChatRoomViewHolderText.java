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
import com.lvshandian.lemeng.utils.GrademipmapUtils;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.session.emoji.MoonUtil;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderText;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hzxuwen on 2016/1/18.
 */
public class ChatRoomViewHolderText extends MsgViewHolderText {
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
        ChatRoomViewHolderHelper.setNameTextView((ChatRoomMessage) message, nameTextView, nameIconView, context);
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
    protected void bindContentView() {
        messageItemLayout.setVisibility(View.VISIBLE);
        messageItemLayout.setPadding(ScreenUtil.dip2px(6),ScreenUtil.dip2px(2),0,ScreenUtil.dip2px(2));

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
        TextView bodyTextView = findViewById(com.netease.nim.uikit.R.id.nim_message_item_text_body);

        bodyTextView.setShadowLayer(1, 0, 2, Color.parseColor("#80000000"));

        if (message.getRemoteExtension() != null) {
            JSONObject obj = null;
            try {
                obj = new JSONObject(message.getRemoteExtension().toString());
                int level = obj.getInt("level");
                levelLeft.setImageResource(GrademipmapUtils.LevelImg[Integer.valueOf(level) - 1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String name = null;
        if (message.getFromNick() != null) {
            name = message.getFromNick() + ":";
        } else if (((ChatRoomMessage) message).getChatRoomMessageExtension().getSenderNick() != null) {
            name = ((ChatRoomMessage) message).getChatRoomMessageExtension().getSenderNick() + ":";
        } else if (message.getFromAccount() != null) {
            name = message.getFromAccount().substring(4, message.getFromAccount().length()) + ":";
        }

        bodyTextView.setGravity(Gravity.CENTER_VERTICAL);
        bodyTextView.setTextColor(Color.WHITE);
//        bodyTextView.setPadding(ScreenUtil.dip2px(6), 0, 0, 0);
        MoonUtil.identifyFaceExpression(NimUIKit.getContext(), bodyTextView, name, getDisplayText(), ImageSpan.ALIGN_BOTTOM);
        bodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
        bodyTextView.setOnLongClickListener(longClickListener);
        bodyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick();
            }
        });
    }

}
