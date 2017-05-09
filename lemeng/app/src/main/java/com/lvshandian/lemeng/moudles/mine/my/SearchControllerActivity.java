package com.lvshandian.lemeng.moudles.mine.my;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.UrlBuilder;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.bean.UserBean;
import com.lvshandian.lemeng.httprequest.HttpDatas;
import com.lvshandian.lemeng.httprequest.RequestCode;
import com.lvshandian.lemeng.moudles.mine.my.adapter.UserControllerAdapter;
import com.lvshandian.lemeng.utils.JsonUtil;
import com.lvshandian.lemeng.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.Bind;

/**
 * 搜索场控页面
 * Created by zz on 2016/11/22.
 */
public class SearchControllerActivity extends BaseActivity {

    @Bind(R.id.iv_private_chat_back)
    ImageView ivPrivateChatBack;
    @Bind(R.id.et_search_input)
    EditText etSearchInput;
    @Bind(R.id.tv_search_btn)
    TextView tvSearchBtn;
    @Bind(R.id.ll_title)
    RelativeLayout llTitle;
    @Bind(R.id.lv_search)
    ListView lvSearch;

    private List<UserBean> userlist = new ArrayList<UserBean>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString(HttpDatas.info);


            switch (msg.what) {
                //关注请求接收数据
                case RequestCode.SEACH_USER:
                    LogUtils.i(json.toString());
                    userlist = JsonUtil.json2BeanList(json, UserBean.class);
                    if (userlist.size() == 0) {
                        showToast("没有搜索到用户");
                    } else {
                        fillUI();
                    }
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_index;
    }

    @Override
    protected void initListener() {
        tvSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etSearchInput.getText().toString())) {
                    showToast("请输入您要搜索的信息");
                } else {
                    seachUser(etSearchInput.getText().toString());
                }
            }
        });

        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserBean userBean = userlist.get(position);
                String id1 = userBean.getId();
                Intent intent = new Intent(mContext, OtherPersonHomePageActivity.class);
                intent.putExtra(getString(R.string.visiti_person), id1);
                startActivity(intent);
            }
        });

    }

    public static SearchControllerActivity searchControllerActivity;

    @Override
    protected void initialized() {
        searchControllerActivity = this;
        ivPrivateChatBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post("reflash");
                finish();
            }
        });
    }

    public static SearchControllerActivity getInstance() {
        if (searchControllerActivity == null) {
            searchControllerActivity = new SearchControllerActivity();
        }
        return searchControllerActivity;
    }

    @Override
    public void onClick(View v) {

    }

    public static void reflash() {
        EventBus.getDefault().post("reflash");
    }

    private void seachUser(String user) {
        userlist.clear();
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("id", user);
        map.put("userId", appUser.getId());
        httpDatas.getNewDataCharServer("搜索用户", Request.Method.GET, UrlBuilder.SEARCH_CONTROLLER, map, mHandler, RequestCode.SEACH_USER);
    }

    private void fillUI() {
        lvSearch.setAdapter(new UserControllerAdapter(userlist, getContext(), appUser.getId()));
    }


}
