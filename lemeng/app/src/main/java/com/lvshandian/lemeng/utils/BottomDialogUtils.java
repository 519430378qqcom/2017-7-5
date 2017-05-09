package com.lvshandian.lemeng.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.lvshandian.lemeng.R;


/**
 * Created by pjz on 2017/1/6.
 * 屏幕底部弹出dialog
 */
public class BottomDialogUtils {

    public static PopupWindow popupWindow;
    public static View currentView;
    public static View showButtoDialog(Context context,View v,int layout){
        currentView = v;
        View view = LayoutInflater.from(context).inflate(layout, null);
        popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        return view;
    }
    public static void dismissPopWindow(){
        if (popupWindow!=null){
            popupWindow.dismiss();
        }
    }

     public static void showPopWindows(){
         popupWindow.showAtLocation(currentView, Gravity.BOTTOM, 0, 0);
     }
}
