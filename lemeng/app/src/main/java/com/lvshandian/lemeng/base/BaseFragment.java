package com.lvshandian.lemeng.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.bean.AppUser;
import com.lvshandian.lemeng.bean.JoinRoomBean;
import com.lvshandian.lemeng.bean.LiveListBean;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.moudles.index.live.WatchLiveActivity;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.lvshandian.lemeng.utils.NetWorkUtil;
import com.lvshandian.lemeng.utils.SharedPreferenceUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * 功能描述：对Fragment类进行扩展
 *
 * @author zhang
 */
public abstract class BaseFragment extends Fragment {

    protected Context mContext;
    protected View view;
    protected HttpDatas httpDatas;
    protected UrlBuilder urlBuilder;
    protected AppUser appUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = getLayoutId();
        view = inflater.inflate(layoutId, null);
        ButterKnife.bind(this, view);
        mContext = this.getActivity();
//        appUser = (AppUser) CacheUtils.readObject(mContext, CacheUtils.USERINFO);
        appUser = SharedPreferenceUtils.getUserInfo(mContext);
        httpDatas = new HttpDatas(mContext, view);
        urlBuilder = new UrlBuilder();
        preliminary();
        return view;
    }


    @Override
    public void onResume() {
        //友盟统计
//        MobclickAgent.onPageStart("");
//        MobclickAgent.onResume(mContext);
        super.onResume();
//        appUser = (AppUser) CacheUtils.readObject(mContext, CacheUtils.USERINFO);
        appUser = SharedPreferenceUtils.getUserInfo(mContext);
    }

    @Override
    public void onPause() {
        super.onPause();
        //友盟统计
//        MobclickAgent.onPageEnd("");
//        MobclickAgent.onPause(mContext);
    }

    /**
     * 向用户展示信息前的准备工作在这个方法里处理
     */
    protected void preliminary() {

        // 初始化数据
        initialized();

        // 初始化组件
        initListener();
    }

    /**
     * 获取全局的Context
     *
     * @return {@link #mContext = this.getApplicationContext();}
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 布局文件ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化监听
     */
    protected abstract void initListener();

    /**
     * 初始化数据
     */
    protected abstract void initialized();


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    public void gotoActivity(Class<? extends Activity> clazz, boolean finish) {
        Intent intent = new Intent(getActivity(), clazz);
        this.startActivity(intent);
        if (finish) {
            getActivity().finish();
        }
        getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);

    }

    public void gotoActivity(Class<? extends Activity> clazz, boolean finish, Bundle pBundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }

        this.startActivity(intent);
        if (finish) {
            getActivity().finish();
        }
        getActivity().overridePendingTransition(R.anim.activity_move_in_start, R.anim.activity_move_out_start);
    }

    /**
     * 进入直播间
     *
     * @author sll
     * @time 2016/12/16 17:14
     */

    public void ifEnter(final String wyRoomId,final String mVideoPath) {
        if (NetWorkUtil.getConnectedType(mContext) == 0) {
            initDialog();
            baseDialogTitle.setText("当前为移动网络,是否继续观看");
            baseDialogLeft.setText("取消观看");
            baseDialogRight.setText("继续观看");
            baseDialogLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (baseDialog != null && baseDialog.isShowing()) {
                        baseDialog.dismiss();
                    }
                }
            });
            baseDialogRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (baseDialog != null && baseDialog.isShowing()) {
                        baseDialog.dismiss();
                    }
//                    ifEnterGo(live);
                    startActivityToWatch(wyRoomId,mVideoPath);
                }
            });
        } else {
//            ifEnterGo(live);
            startActivityToWatch(wyRoomId,mVideoPath);
        }

    }


    /**
     * 判断是否进入过该私密直播
     *
     * @author sll
     * @time 2016/12/16 17:14
     */
    public void ifEnterGo(final LiveListBean live) {
        //进入直播间请求
        String url = UrlBuilder.CHARGE_SERVER_URL + UrlBuilder.IF_ENTER;
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("roomId", live.getRooms().getId() + "");
        hashMap.put("userId", appUser.getId());
        String json = new org.json.JSONObject(hashMap).toString();
        LogUtils.e("Json:　" + json);
        OkHttpUtils.get().url(url)
                .params(hashMap)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(com.squareup.okhttp.Request request, Exception e) {
                        LogUtils.i("WangYi_secret", "是否进入过该私密直播: " + e.toString());
                    }

                    @Override
                    public void onResponse(String response) {
                        LogUtils.i("WangYi_secret", "是否进入过该私密直播: " + response);
                        JoinRoomBean joinRoom = JsonUtil.json2Bean(response, JoinRoomBean.class);
                        if (joinRoom != null && joinRoom.isSuccess() && joinRoom.getCode() != 1) {
                            //第一次进入
                            if (live != null && !TextUtils.isEmpty(live.getRooms().getPrivateFlag() + "")
                                    && ((live.getRooms().getPrivateFlag() + "")).equals("1")) {
                                joinSecret(live);
                            } else {
//                                startActivityToWatch(live);
                            }
                        } else {
                            //不是第一次，直接进入
//                            startActivityToWatch(live);
                        }
                    }
                });
    }

    /**
     * 支付进入该私密直播，进行金币支付
     * "createrId":createId , @"entrantId":userId ,@"coinNum":coinNum
     *
     * @author sll
     * @time 2016/12/16 17:14
     */
    private void updateCoin(final LiveListBean live) {
        String url = UrlBuilder.CHARGE_SERVER_URL + UrlBuilder.UPDATE_COIN;
        LogUtils.e("WangYi_secret", "url: " + url);
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("createrId", live.getRooms().getUserId() + "");
        hashMap.put("entrantId", appUser.getId());
        hashMap.put("coinNum", live.getRooms().getRoomPay() + "");
        String json = new org.json.JSONObject(hashMap).toString();
        LogUtils.e("Json:　" + json);
        OkHttpUtils.get().url(url)
                .params(hashMap)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(com.squareup.okhttp.Request request, Exception e) {
                    }

                    @Override
                    public void onResponse(String response) {
                        JoinRoomBean joinRoom = JsonUtil.json2Bean(response, JoinRoomBean.class);
                        if (joinRoom != null && joinRoom.isSuccess() && joinRoom.getCode() == 1) {
//                            startActivityToWatch(live);
                        } else {
                            //账户余额不足
                            showToast("账户余额不足");
                        }
                    }
                });

    }

    /**
     * 跳转到观看页面
     *
     * @author sll
     * @time 2016/12/16 16:58
     */
    private void startActivityToWatch(String wyRoomId,String mVideoPath) {
        Intent intent = new Intent(getActivity(), WatchLiveActivity.class);
        intent.putExtra("wyRoomId", wyRoomId);
        intent.putExtra("mVideoPath", mVideoPath);
        startActivity(intent);
    }

    /**
     * 私密直播时弹出，须支付或输入密码
     *
     * @author sll
     * @time 2016/12/16 15:59
     */
    private void joinSecret(final LiveListBean live) {
        final Dialog dialog = new Dialog(getActivity(), R.style.homedialog);
        final View view = View.inflate(getActivity(), R.layout.dialog_join_secret, null);
        final TextView joinSecretPrompt = (TextView) view.findViewById(R.id.join_secret_prompt);
        final LinearLayout joinSecretCancel = (LinearLayout) view.findViewById(R.id
                .join_secret_cancel);
        joinSecretPrompt.setText("需要密码或" + live.getRooms().getRoomPay() + "金币进入该房间");
        //支付
        view.findViewById(R.id.join_secret_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCoin(live);
                dialog.dismiss();
            }
        });
        //密码
        view.findViewById(R.id.join_secret_pw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinForPw(live);
                dialog.dismiss();
            }
        });
        //取消
        joinSecretCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.show();
    }

    /**
     * 支付密码进入私密直播
     *
     * @author sll
     * @time 2016/12/16 16:34
     */
    private void joinForPw(final LiveListBean live) {
        final Dialog dialog = new Dialog(getActivity(), R.style.homedialog);
        final View view = View.inflate(getActivity(), R.layout.dialog_join_secret_pwd, null);
        final EditText pwdEdit = (EditText) view.findViewById(R.id.join_secret_pwd_edit);
        //取消
        view.findViewById(R.id.join_secret_pwd_cancel).setOnClickListener(new View
                .OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //确定
        view.findViewById(R.id.join_secret_pwd_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(pwdEdit.getText().toString())) {
                    showToast("请输入密码");
                } else if (pwdEdit.getText().toString().equals(live.getRooms().getRoomPw())) {
                    //密码正确，进入直播间
//                    startActivityToWatch(live);
                    dialog.dismiss();
                } else {
                    showToast("密码错误，请确认后再输入");
                }
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        dialog.show();
    }

    /**
     * 吐司
     *
     * @param msg
     */
    public void showToast(String msg) {
        com.lvshandian.lemeng.utils.ToastUtils.showMessageDefault(mContext, msg);
    }

    public TextView baseDialogTitle, baseDialogLeft, baseDialogRight;
    public Dialog baseDialog;

    /**
     * 自定义dialog
     */
    public void initDialog() {
        if (baseDialog == null) {
            baseDialog = new Dialog(mContext, R.style.CustomDialog);
        }
        baseDialog.setCanceledOnTouchOutside(true);
        baseDialog.setCancelable(true);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.custom_dialog, null);

        baseDialog.setContentView(view);
        WindowManager.LayoutParams params = baseDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        baseDialog.getWindow().setAttributes(params);
        baseDialog.show();

        baseDialogTitle = (TextView) view.findViewById(R.id.tvTitle);
        baseDialogLeft = (TextView) view.findViewById(R.id.btnLeft);
        baseDialogRight = (TextView) view.findViewById(R.id.btnRight);
    }

}
