package com.lvshandian.lemeng.moudles.mine.my;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.moudles.mine.activity.BigImageActivity;
import com.lvshandian.lemeng.moudles.mine.bean.PhotoBean;
import com.lvshandian.lemeng.moudles.mine.my.adapter.AllPhoneAdapter;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * Created by lsd on 2017/4/14 0014.
 */

public class AllPhoneActivity extends BaseActivity {
    @Bind(R.id.mygrid)
    GridView mygrid;
    public List<PhotoBean> list = new ArrayList<PhotoBean>();
    public List<String> imgList = new ArrayList<>();

    /**
     * 右上角弹框
     */
    private PopupWindow editPopup;

    private AllPhoneAdapter allPhoneAdapter;

    private String userId;
    private String isShow;

    private String picUrl;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);

            switch (msg.what) {
                case RequestCode.MY_PHOTO_LOAD://图片请求列表
                    List<PhotoBean> listAdd = JsonUtil.json2BeanList(json.toString(), PhotoBean.class);

                    imgList.clear();
                    for (int i = 0, j = listAdd.size(); i < j; i++) {
                        imgList.add(listAdd.get(i).getUrl());
                    }

                    list.clear();
                    list.addAll(listAdd);
                    allPhoneAdapter.notifyDataSetChanged();

                    mygrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Intent intent = new Intent(getContext(), PhotoDetails.class);
//                            intent.putExtra("photo", list.get(position));
//                            intent.putExtra("isShow", isShow);
//                            startActivity(intent);
                            startActivity(new Intent(mContext, BigImageActivity.class).putStringArrayListExtra("imageList", (ArrayList<String>) imgList).putExtra("clickPosition", position));
                        }
                    });

                    if (isShow != null && isShow.equals("isShow")) {
                        mygrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                getEditPopup(position);
                                return true;
                            }
                        });
                    }
                    break;
                case RequestCode.MY_PHOTO_DELETE_CODE://图片请求列表
                    PhotoBean photoBean = new PhotoBean();
                    photoBean.setId("yes");
                    EventBus.getDefault().post(photoBean);
                    //图片删除成功
                    showToast("图片删除成功");
                    requestPhoto();
                    break;
                case RequestCode.USER_TAG:
                    LogUtils.e("设置头像返回: " + json);
                    showToast("设置头像成功");
                    sendUserToWangYi();
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_all_phone;
    }

    @Override
    protected void initListener() {
        allPhoneAdapter = new AllPhoneAdapter(mContext, list);
        mygrid.setAdapter(allPhoneAdapter);
    }


    /**
     * 请求图片列表
     */
    private void requestPhoto() {
        ConcurrentHashMap map = new ConcurrentHashMap<>();
        httpDatas.getDataForJsoNoloading("图片请求列表", Request.Method.GET, UrlBuilder.myPhoto(userId), map, mHandler, RequestCode.MY_PHOTO_LOAD);
    }

    @Override
    protected void initialized() {
        initTitle("", "相册", null);
        userId = getIntent().getStringExtra("userId");
        isShow = getIntent().getStringExtra("isShow");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_titlebar_left:
                defaultFinish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPhoto();
    }


    /**
     * 右上角举报和加入黑名单弹框
     */
    public void getEditPopup(final int position) {
        editPopup = new PopupWindow(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.pic_edit_popup, null);
        editPopup.setContentView(view);
        editPopup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        editPopup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        editPopup.setFocusable(true);
        editPopup.setBackgroundDrawable(new BitmapDrawable());
        editPopup.setOutsideTouchable(true);

        backgroundAlpha(0.5f);

        editPopup.setAnimationStyle(R.style.mypopwindow_anim_style);
        editPopup.showAtLocation(mygrid, Gravity.BOTTOM, 0, 0);
        editPopup.update();
        editPopup.setOnDismissListener(new PopOnDismissListner());

        Button btn_set_head = (Button) view.findViewById(R.id.btn_set_head);
        Button btn_delete = (Button) view.findViewById(R.id.btn_delete);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        btn_set_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picUrl = list.get(position).getUrl();
                ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
                map.put("id", appUser.getId());
                map.put("picUrl", picUrl);
                httpDatas.getNewDataCharServer("设置头像信息", Request.Method.POST, UrlBuilder.user, map, mHandler, RequestCode.USER_TAG);
                editPopup.dismiss();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConcurrentHashMap map = new ConcurrentHashMap<>();
                httpDatas.getDataForJson("删除图片", Request.Method.DELETE, UrlBuilder.photoDelete(list.get(position).getId()), map, mHandler, RequestCode.MY_PHOTO_DELETE_CODE);
                editPopup.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPopup.dismiss();
            }
        });
    }

    private void backgroundAlpha(float alpha) {
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = alpha;
        this.getWindow().setAttributes(params);
    }


    private class PopOnDismissListner implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    /**
     * 发送修改后的信息给网易云信
     */
    private void sendUserToWangYi() {
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        fields.put(UserInfoFieldEnum.Name, appUser.getNickName());
        fields.put(UserInfoFieldEnum.AVATAR, picUrl);
        NIMClient.getService(UserService.class).updateUserInfo(fields)
                .setCallback(new RequestCallbackWrapper<Void>() {

                    @Override
                    public void onResult(int code, Void aVoid, Throwable throwable) {
                        if (code == ResponseCode.RES_SUCCESS) {
                            LogUtils.i("WangYi", "上传昵称头像成功");
                        } else {
                            LogUtils.i("WangYi", "上传昵称头像失败");
                        }
                    }
                });
    }
}
