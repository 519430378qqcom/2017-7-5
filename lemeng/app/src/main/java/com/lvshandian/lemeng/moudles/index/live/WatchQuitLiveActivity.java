package com.lvshandian.lemeng.moudles.index.live;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.base.BaseActivity;
import com.lvshandian.lemeng.bean.LiveListBean;
import com.lvshandian.lemeng.moudles.mine.my.OtherPersonHomePageActivity;
import com.lvshandian.lemeng.utils.FastBlurUtil;
import com.lvshandian.lemeng.widget.AvatarView;

import butterknife.Bind;

/**
 * 观众在直播间主播退出后的界面
 */
public class WatchQuitLiveActivity extends BaseActivity {
    @Bind(R.id.img_bg)
    ImageView img_bg;
    @Bind(R.id.iv_head)
    AvatarView iv_head;
    @Bind(R.id.nickName)
    TextView nickName;
    @Bind(R.id.ll_go_home)
    LinearLayout ll_go_home;
    @Bind(R.id.ll_home_back)
    LinearLayout ll_home_back;

    /**
     * 主播对象实体类
     */
    private LiveListBean liveListBean;

    /**
     * 主播id
     */
    private String userId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_quit_live_watch;
    }

    @Override
    protected void initListener() {
        ll_go_home.setOnClickListener(this);
        ll_home_back.setOnClickListener(this);
    }

    @Override
    protected void initialized() {
        liveListBean = (LiveListBean) getIntent().getSerializableExtra("liveListBean");
        userId = liveListBean.getRooms().getUserId()+"";

        final String picUrl = liveListBean.getPicUrl();

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap blurBitmap2 = FastBlurUtil.GetUrlBitmap(picUrl, 4);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        img_bg.setImageBitmap(blurBitmap2);
                    }
                });
            }
        }).start();

        iv_head.setAvatarUrl(picUrl);
        nickName.setText(liveListBean.getNickName());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_go_home:
                Intent intent = new Intent(mContext, OtherPersonHomePageActivity.class);
                intent.putExtra(getString(R.string.visit_person), userId);
                startActivity(intent);
                finish();
                break;
            case R.id.ll_home_back:
                finish();
                break;
        }
    }


}
