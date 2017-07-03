package com.lvshandian.lemeng.adapter.mine;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.activity.mine.SearchControllerActivity;
import com.lvshandian.lemeng.entity.UserBean;
import com.lvshandian.lemeng.net.NewSdkHttpResult;
import com.lvshandian.lemeng.net.UrlBuilder;
import com.lvshandian.lemeng.widget.view.CircleImageView;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.ToastUtils;

import org.kymjs.kjframe.Core;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;


/**
 *
 */
public class UserControllerAdapter extends BaseAdapter {
    private List<UserBean> users;
    private Context context;
    private String appuserid;

    public UserControllerAdapter(List<UserBean> users, Context context, String appuserid) {
        this.users = users;
        this.context = context;
        this.appuserid = appuserid;

    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.item_search, null);
        TextView tv_nick_name = (TextView) convertView.findViewById(R.id.nick_name);
        TextView tv_data = (TextView) convertView.findViewById(R.id.tv_data);
        TextView tv_cancle = (TextView) convertView.findViewById(R.id.tv_cancle);
        CircleImageView iv_pic = (CircleImageView) convertView.findViewById(R.id.iv_pic);
        final UserBean u = users.get(position);
        Core.getKJBitmap().display(iv_pic, u.getPicUrl());
        tv_nick_name.setText(u.getNickName());
        tv_data.setText(u.getSignature());
        if (u.getControllFlag() == 1) {
            setText(tv_cancle, context.getResources().getString(R.string.cancel_field_control));
        } else {
            setText(tv_cancle, context.getResources().getString(R.string.set_field_control));
        }
        tv_cancle.setOnClickListener(new MyOnclick(position, tv_cancle));
        return convertView;
    }


    public void setText(TextView tv_cancle, String stringText) {
        if (stringText.equals(context.getResources().getString(R.string.cancel_field_control))) {
            tv_cancle.setText(context.getResources().getString(R.string.cancel_field_control));
            tv_cancle.setTextColor(context.getResources().getColor(R.color.black));
            tv_cancle.setBackground(context.getResources().getDrawable(R.drawable.selector_contrler));
        } else {
            tv_cancle.setText(context.getResources().getString(R.string.set_field_control));
            tv_cancle.setTextColor(context.getResources().getColor(R.color.main));
            tv_cancle.setBackground(context.getResources().getDrawable(R.drawable.selector_contrler_select));
        }


    }

    class MyOnclick implements View.OnClickListener {

        private int position;
        private TextView tv;

        public MyOnclick(int pos, TextView tv) {
            this.position = pos;
            this.tv = tv;
        }

        @Override
        public void onClick(View v) {

            String textString = tv.getText().toString();
            if (textString.equals(context.getResources().getString(R.string.set_field_control))) {
                requestControllerAdd(users.get(position).getId(), tv);
            } else {
                requestControllerDelete(users.get(position).getId(), tv);
            }
        }
    }

    /**
     * 刪除场控人员
     */
    private void requestControllerDelete(String userControlId, final TextView tv) {

        RequestParams params = new RequestParams(UrlBuilder.CHARGE_SERVER_URL + UrlBuilder.MY_CONTROLLER_DELETE);
        params.addQueryStringParameter("userId", appuserid);
        params.addQueryStringParameter("userControlId", userControlId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e("requestControllerDelete" + result.toString());
                NewSdkHttpResult newSdkhttpResult = JsonUtil.json2Bean(result.toString(), NewSdkHttpResult.class);
                if (newSdkhttpResult.isSuccess()) {
                    if (newSdkhttpResult.getCode() == 1) {
                        setText(tv, context.getResources().getString(R.string.set_field_control));
                    } else if (newSdkhttpResult.getCode() == 2 || newSdkhttpResult.getCode() == 3) {
                        ToastUtils.showMessageCenter(context, newSdkhttpResult.getMsg());
                    }
                    SearchControllerActivity.getInstance().reflash();
                } else {
                    ToastUtils.showMessageCenter(context, context.getResources().getString(R.string.operation_failure));
                    setText(tv, context.getResources().getString(R.string.cancel_field_control));
                }

            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });

    }

    /**
     * 添加场控人员
     */
    private void requestControllerAdd(String userControlId, final TextView tv) {

        RequestParams params = new RequestParams(UrlBuilder.CHARGE_SERVER_URL + UrlBuilder.MY_CONTROLLER_ADD);
        params.addQueryStringParameter("userId", appuserid);
        params.addQueryStringParameter("userControlId", userControlId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                NewSdkHttpResult newSdkhttpResult = JsonUtil.json2Bean(result.toString(), NewSdkHttpResult.class);
                if (newSdkhttpResult.isSuccess()) {
                    if (newSdkhttpResult.getCode() == 1) {
                        setText(tv, context.getResources().getString(R.string.cancel_field_control));
                    } else if (newSdkhttpResult.getCode() == 2 || newSdkhttpResult.getCode() == 3) {
                        ToastUtils.showMessageCenter(context, newSdkhttpResult.getMsg());
                    }
                    SearchControllerActivity.getInstance().reflash();
                } else {
                    ToastUtils.showMessageCenter(context, context.getResources().getString(R.string.operation_failure));
                    setText(tv, context.getResources().getString(R.string.set_field_control));
                }
            }

            //请求异常后的回调方法
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            //主动调用取消请求的回调方法
            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });


    }
}
