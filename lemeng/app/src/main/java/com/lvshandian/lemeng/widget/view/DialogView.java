package com.lvshandian.lemeng.widget.view;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lvshandian.lemeng.R;


/**
 * Created by Administrator on 2016/9/24.
 * <p/>
 * 创建一个DialogView
 */
public class DialogView extends PopupWindow {


    /**
     * 头像上传弹出框
     *
     * @param context
     * @param parent
     * @param mycallback
     */
    private MyCameraCallback mycamerback;

    public DialogView(final Context context, final View parent, String tag, String titleOne, String titleTwo, String titleThree,
                      final MyCameraCallback mycallback) {
        super(context);
        this.mycamerback = mycallback;

        if (context == null || parent == null) {
            return;
        }

        final View view = LayoutInflater.from(context).inflate(
                R.layout.dialogview_head, null);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(context.getResources().getDrawable(
                R.drawable.popwindow_bg));


        TextView tv_yse = (TextView) view.findViewById(R.id.tv_yse);

        tv_yse.setText(titleOne);
        TextView tv_no = (TextView) view.findViewById(R.id.tv_no);
        tv_no.setText(titleTwo);
        TextView tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
        tv_cancle.setText(titleThree);
        /**
         * 相机
         */
        tv_yse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogView.this.dismiss();
                mycamerback.refreshCallback(1);
            }
        });
        /**
         * 图库
         */
        tv_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogView.this.dismiss();
                mycamerback.refreshCallback(2);
            }
        });
        /**
         *
         *取消
         */
        tv_cancle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DialogView.this.dismiss();
                mycamerback.refreshCallback(3);
            }
        });
        setContentView(view);
        setFocusable(true);
        showAtLocation((View) parent.getParent(), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 相机接口
     */
    public interface MyCameraCallback {

        void refreshCallback(int tag);

    }

}
