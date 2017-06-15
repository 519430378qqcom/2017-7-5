package com.lvshandian.lemeng.moudles.mine.my.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.bean.ControllerBean;
import com.lvshandian.lemeng.httprequest.NewSdkHttpResult;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.ToastUtils;
import com.lvshandian.lemeng.widget.view.CircleImageView;
import com.tandong.bottomview.view.BottomView;

import org.kymjs.kjframe.Core;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;


/**
 *
 */
public class ControllerBaseAdapter extends BaseAdapter {
    private List<ControllerBean> mDatas;
    private Context context;
    private String appuserid;
    BottomView familySelectView;

    public ControllerBaseAdapter(List<ControllerBean> mDatas, Context context, String appuserid, BottomView familySelectView) {
        this.mDatas = mDatas;
        this.context = context;
        this.appuserid = appuserid;
        this.familySelectView = familySelectView;

    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
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
        final ControllerBean u = mDatas.get(position);
        Core.getKJBitmap().display(iv_pic, u.getPicUrl());
        tv_nick_name.setText(u.getNickName());
        tv_data.setText(u.getSignature());
        tv_cancle.setOnClickListener(new MyOnclick(position, tv_cancle));
        return convertView;
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

            requestControllerDelete(mDatas.get(position).getId() + "", position);
        }
    }

    /**
     * 刪除场控人员
     */
    private void requestControllerDelete(String userControlId, final int position) {

        RequestParams params = new RequestParams(UrlBuilder.CHARGE_SERVER_URL + UrlBuilder.myControllerDelete);
        params.addQueryStringParameter("userId", appuserid);
        params.addQueryStringParameter("userControlId", userControlId);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtils.e("requestControllerDelete" + result.toString());
                NewSdkHttpResult newSdkhttpResult = JsonUtil.json2Bean(result.toString(), NewSdkHttpResult.class);
                if (newSdkhttpResult.isSuccess()) {
                    if (newSdkhttpResult.getCode() == 1) {
                        mDatas.remove(position);
                        notifyDataSetChanged();
                        if (null != familySelectView && mDatas.size() == 0) {
                            familySelectView.dismissBottomView();
                        }
                    } else if (newSdkhttpResult.getCode() == 2 || newSdkhttpResult.getCode() == 3) {
                        ToastUtils.showMessageCenter(context, newSdkhttpResult.getMsg());
                    }
                } else {
                    ToastUtils.showMessageCenter(context, "操作失败");
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
