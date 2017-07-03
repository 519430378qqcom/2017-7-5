package com.lvshandian.lemeng.activity.live;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.activity.BaseActivity;
import com.lvshandian.lemeng.engine.service.VoiceService;
import com.lvshandian.lemeng.entity.AudioPlayInfo;
import com.lvshandian.lemeng.entity.PlayerSearchBean;
import com.lvshandian.lemeng.entity.db.Audios;
import com.lvshandian.lemeng.adapter.AudioPlayerAdapter;
import com.lvshandian.lemeng.net.UrlBuilder;
import com.lvshandian.lemeng.widget.refresh.SwipeRefresh;
import com.lvshandian.lemeng.widget.refresh.SwipeRefreshLayout;
import com.lvshandian.lemeng.widget.view.LoadingDialog;
import com.lvshandian.lemeng.utils.PermisionUtils;
import com.lvshandian.lemeng.utils.TextUtils;
import com.netease.nim.uikit.team.activity.JsonUtil;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.FindMultiCallback;
import org.litepal.crud.callback.SaveCallback;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 歌曲播放界面
 * Created by shang on 2017/3/27.
 */
public class AudioPlayerActivity extends BaseActivity implements SwipeRefresh.OnRefreshListener, SwipeRefreshLayout.OnPullUpRefreshListener  {
    private AudioPlayerAdapter audioPlayerAdapter;
    private RecyclerView audioRecyclerView;
    private ImageView iv_back;
    private TextView tv_search_btn;
    private EditText et_search_input;
    private SwipeRefreshLayout mrlLayout;

    private boolean isSearch;//是否为搜索

    private List<Audios> songList = new ArrayList<>();
    /**
     * 是否取消下载
     */
    private boolean cancelUpdate = false;

    /**
     * 正在播放的歌曲id
     */
    public static String playingId;

    /**
     * 正在暂停的歌曲id
     */
    public static String pauseingId;

    /**
     * 是否取消下载
     */
    public static final int DOWN_LOAD_MP3 = 10000;

    /**
     * 请求Loading
     */
    private LoadingDialog mLoading;

    private PlayingEndBroadcastReceiver receiver;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DOWN_LOAD_MP3:
                    int position = msg.arg1;
                    Audios audios = songList.get(position);
                    audios.setDownload(true);
                    audios.saveOrUpdateAsync("songname=? and artistname=?", audios.getSongname(),
                            audios.getArtistname()).listen(new SaveCallback() {
                        @Override
                        public void onFinish(boolean success) {
                            if (success) {
                                showToast(getString(R.string.download_finish));
                            } else {
                                showToast(getString(R.string.save_failure));
                            }
                        }
                    });
                    audioPlayerAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("SONG_PLAYING_END");
        receiver = new PlayingEndBroadcastReceiver();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_audio_player;
    }

    @Override
    protected void initListener() {
        //设置刷新逻辑
        mrlLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
        mrlLayout.setOnRefreshListener(this);
        mrlLayout.setOnPullUpRefreshListener(this);
        mrlLayout.setColorSchemeColors(getResources().getColor(R.color.main));

        iv_back.setOnClickListener(this);
        tv_search_btn.setOnClickListener(this);
        audioPlayerAdapter.setOnAudioClickListener(new AudioPlayerAdapter.OnAudioClickListener() {
            @Override
            public void OnAudioItemClick(int position, String str) {
                if (str.equals(getString(R.string.play))||str.equals(getString(R.string.go_on))) {
                    String sdpath = Environment.getExternalStorageDirectory() + "/lemeng/";
                    String fileUrl = sdpath + "download/";
                    String uri = fileUrl + songList.get(position).getSongid() + ".mp3";
                    String lrc = fileUrl + songList.get(position).getSongid() + ".lrc";
                    bindVoiceSerive(uri, lrc);

                    playingId = songList.get(position).getSongid();
                    pauseingId = "";
                    for (int i = 0, j = songList.size(); i < j; i++) {
                        songList.get(i).setPlaying(false);
                        songList.get(i).setPause(false);
                    }
                    songList.get(position).setPlaying(true);
                    audioPlayerAdapter.notifyDataSetChanged();
                } else if (str.equals(getString(R.string.pause))) {
                    String sdpath = Environment.getExternalStorageDirectory() + "/lemeng/";
                    String fileUrl = sdpath + "download/";
                    String uri = fileUrl + songList.get(position).getSongid() + ".mp3";
                    String lrc = fileUrl + songList.get(position).getSongid() + ".lrc";
                    bindVoiceSerive(uri, lrc);

                    pauseingId = songList.get(position).getSongid();
                    playingId = "";
                    for (int i = 0, j = songList.size(); i < j; i++) {
                        songList.get(i).setPlaying(false);
                        songList.get(i).setPause(false);
                    }
                    songList.get(position).setPause(true);
                    audioPlayerAdapter.notifyDataSetChanged();
                }else {
                    Audios audios = songList.get(position);
                    getSongDownloadUrl(audios.getSongid(), position);
                }
            }

            @Override
            public void OnAudioLongItemClick(final int position, String str) {
                if (str.equals(getString(R.string.play))) {
                    final AlertDialog.Builder normalDialog = new AlertDialog.Builder(mContext);
                    normalDialog.setCancelable(true);
                    normalDialog.setMessage(getString(R.string.if_delete_the_song));
                    normalDialog.setTitle(getString(R.string.hint));
                    normalDialog.setPositiveButton(getString(R.string.confirm),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DataSupport.deleteAll(Audios.class, "songid = ?", songList
                                            .get(position).getSongid());
                                    String sdpath = Environment.getExternalStorageDirectory() +
                                            "/lemeng/";
                                    String fileUrl = sdpath + "download/";
                                    String uri = fileUrl + songList.get(position).getSongid() + "" +
                                            ".mp3";
                                    File file = new File(uri);
                                    if (file.exists())
                                        file.delete();
                                    showToast(getString(R.string.delete_success));
                                    songList.remove(position);
                                    audioPlayerAdapter.notifyDataSetChanged();
                                }
                            });
                    normalDialog.show();

                } else if (str.equals(getString(R.string.pause))){
                    showToast(getString(R.string.is_playing_no_delete));
                } else if (str.equals(getString(R.string.go_on))){
                    showToast(getString(R.string.is_go_on_no_delete));
                }else  {
                    showToast(getString(R.string.no_download));
                }
            }
        });
    }

    @Override
    protected void initialized() {
        mrlLayout = (SwipeRefreshLayout) findViewById(R.id.mrl_layout);
        audioRecyclerView = (RecyclerView) findViewById(R.id.audioList);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_search_btn = (TextView) findViewById(R.id.tv_search_btn);
        et_search_input = (EditText) findViewById(R.id.et_search_input);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        audioRecyclerView.setLayoutManager(layoutManager);
        audioPlayerAdapter = new AudioPlayerAdapter(mContext, songList);
        audioRecyclerView.setAdapter(audioPlayerAdapter);
        //
        querySongList();
    }

    /**
     * 从数据库中取出历史记录
     */
    private void querySongList() {
        DataSupport.findAllAsync(Audios.class).listen(new FindMultiCallback() {
            @Override
            public <T> void onFinish(List<T> t) {
                List<Audios> songLi = (List<Audios>) t;
                songList.clear();
                songList.addAll(songLi);

                if (!TextUtils.isEmpty(playingId)) {
                    for (int i = 0, j = songList.size(); i < j; i++) {
                        if (playingId.equals(songList.get(i).getSongid())) {
                            songList.get(i).setPlaying(true);
                        } else {
                            songList.get(i).setPlaying(false);
                        }
                    }
                } else {
                    for (int i = 0, j = songList.size(); i < j; i++) {
                        songList.get(i).setPlaying(false);
                    }
                }

                if (!TextUtils.isEmpty(pauseingId)) {
                    for (int i = 0, j = songList.size(); i < j; i++) {
                        if (pauseingId.equals(songList.get(i).getSongid())) {
                            songList.get(i).setPause(true);
                        } else {
                            songList.get(i).setPause(false);
                        }
                    }
                } else {
                    for (int i = 0, j = songList.size(); i < j; i++) {
                        songList.get(i).setPause(false);
                    }
                }
                audioPlayerAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * 搜索歌曲列表
     *
     * @param str
     */
    private void getSearchSongList(String str) {
        RequestParams params = new RequestParams(String.format(UrlBuilder.BAIDU_SONG_SEARCH, str));
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                finishRefresh();
                if (mLoading != null && mLoading.isShowing()) {
                    mLoading.dismiss();
                }
                PlayerSearchBean searchBean = JsonUtil.json2Bean(result, PlayerSearchBean.class);
                final List<Audios> songBeen = searchBean.getSong();
                if (songBeen == null || songBeen.size() == 0) {
                    showToast(getString(R.string.no_songs_were_found));
                    songList.clear();
                    audioPlayerAdapter.notifyDataSetChanged();
                    return;
                }
                songList.clear();
                songList.addAll(songBeen);

                if (!TextUtils.isEmpty(playingId)) {
                    for (int i = 0, j = songList.size(); i < j; i++) {
                        if (playingId.equals(songList.get(i).getSongid())) {
                            songList.get(i).setPlaying(true);
                        } else {
                            songList.get(i).setPlaying(false);
                        }
                    }
                } else {
                    for (int i = 0, j = songList.size(); i < j; i++) {
                        songList.get(i).setPlaying(false);
                    }
                }

                if (!TextUtils.isEmpty(pauseingId)) {
                    for (int i = 0, j = songList.size(); i < j; i++) {
                        if (pauseingId.equals(songList.get(i).getSongid())) {
                            songList.get(i).setPause(true);
                        } else {
                            songList.get(i).setPause(false);
                        }
                    }
                } else {
                    for (int i = 0, j = songList.size(); i < j; i++) {
                        songList.get(i).setPause(false);
                    }
                }
                audioPlayerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                finishRefresh();
                showToast(getString(R.string.network_error));
                if (mLoading != null && mLoading.isShowing()) {
                    mLoading.dismiss();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                finishRefresh();
                showToast(getString(R.string.network_error));
                if (mLoading != null && mLoading.isShowing()) {
                    mLoading.dismiss();
                }
            }

            @Override
            public void onFinished() {
                finishRefresh();
                if (mLoading != null && mLoading.isShowing()) {
                    mLoading.dismiss();
                }
            }
        });
    }

    /**
     * 获得下载url
     *
     * @param songid
     */

    private void getSongDownloadUrl(String songid, final int position) {
        RequestParams params = new RequestParams(String.format(UrlBuilder.BAIDU_SONG_PLAY, songid));
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                final AudioPlayInfo playInfo = JsonUtil.json2Bean(result, AudioPlayInfo.class);
                PermisionUtils.newInstance().checkWriteStoragePermission(mContext, new PermisionUtils.OnPermissionGrantedLintener() {
                    @Override
                    public void permissionGranted() {
                        downloadMp3(playInfo, position);
                    }
                });
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showToast(getString(R.string.network_error));
            }

            @Override
            public void onCancelled(CancelledException cex) {
                showToast(getString(R.string.network_error));
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 绑定服务，播放音乐
     */
    public void bindVoiceSerive(final String fileUrl, final String lrc) {
        Intent intent = new Intent(mContext, VoiceService.class);
        intent.putExtra("URL", fileUrl);
        intent.putExtra("LRC", lrc);
        mContext.startService(intent);
    }

    /**
     * 下载apk文件
     */
    private void downloadMp3(AudioPlayInfo playInfo, int position) {
        // 启动新线程下载mp3
        new downloadMp3Thread(playInfo, position).start();
    }

    /**
     * 下载文件线程
     */
    private class downloadMp3Thread extends Thread {
        AudioPlayInfo playInfo;
        int position;

        public downloadMp3Thread(AudioPlayInfo playInfo, int position) {
            this.position = position;
            this.playInfo = playInfo;
        }

        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/lemeng/";
                    String mSavePath = sdpath + "download";
                    URL url = new URL(playInfo.getBitrate().getShow_link());
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 创建输入流
                    InputStream is = conn.getInputStream();
                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File apkFile = new File(mSavePath, playInfo.getSonginfo().getSong_id() + "" +
                            ".mp3");
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        if (numread <= 0) {
                            // 下载歌曲完成
//                            Message message = mHandler.obtainMessage();
//                            message.arg1 = position;
//                            message.what = DOWN_LOAD_MP3;
//                            mHandler.sendMessage(message);
                            downLoadLrc(mSavePath, playInfo.getSonginfo().getLrclink(), position, playInfo.getSonginfo().getSong_id());
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void downLoadLrc(String mSavePath, String lrclink, int position, String songid) {
        try {
            URL url = new URL(lrclink);
            // 创建连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            // 创建输入流
            InputStream is = conn.getInputStream();
            File apkFile = new File(mSavePath, songid + "" + ".lrc");
            FileOutputStream fos = new FileOutputStream(apkFile);
            // 缓存
            byte buf[] = new byte[1024];
            // 写入到文件中
            do {
                int numread = is.read(buf);
                if (numread <= 0) {
                    // 下载歌词完成
                    Message message = mHandler.obtainMessage();
                    message.arg1 = position;
                    message.what = DOWN_LOAD_MP3;
                    mHandler.sendMessage(message);
                    break;
                }
                // 写入文件
                fos.write(buf, 0, numread);
            } while (!cancelUpdate);// 点击取消就停止下载.
            fos.close();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                defaultFinish();
                break;
            case R.id.tv_search_btn:
                if (TextUtils.isEmpty(et_search_input.getText().toString().trim())) {
                    isSearch = false;
                    //
                    querySongList();
                } else {
                    isSearch = true;
                    mLoading = new LoadingDialog(mContext);
                    mLoading.show();
                    getSearchSongList(et_search_input.getText().toString().trim());
                }
                break;
        }
    }

    public class PlayingEndBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            for (int i = 0, j = songList.size(); i < j; i++) {
                songList.get(i).setPlaying(false);
                songList.get(i).setPause(false);
            }
            audioPlayerAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        if (isSearch){
            getSearchSongList(et_search_input.getText().toString().trim());
        }else {
            finishRefresh();
        }

    }

    @Override
    public void onPullUpRefresh() {
        if (isSearch){
            getSearchSongList(et_search_input.getText().toString().trim());
        }else {
            finishRefresh();
        }
}

    private void finishRefresh() {
        mrlLayout.setRefreshing(false);
        mrlLayout.setPullUpRefreshing(false);
    }

}
