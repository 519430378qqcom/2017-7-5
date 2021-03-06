package com.lvshandian.lemeng.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvshandian.lemeng.R;
import com.lvshandian.lemeng.entity.db.Audios;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 */

public class AudioPlayerAdapter extends RecyclerView.Adapter<AudioPlayerAdapter
        .AudioPlayerViewHolder> {
    private Context mContext;
    private List<Audios> songList;
    private OnAudioClickListener onAudioClickListener;

    public AudioPlayerAdapter(Context mContext, List<Audios> songList) {
        this.mContext = mContext;
        this.songList = songList;
    }

    @Override
    public AudioPlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.audio_list, parent, false);
        AudioPlayerViewHolder viewHolder = new AudioPlayerViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final AudioPlayerViewHolder holder, int position) {

        Audios listBean = songList.get(position);
        List<Audios> listBean1 = DataSupport.where("songid = ?", listBean.getSongid()).find
                (Audios.class);
        String sdpath = Environment.getExternalStorageDirectory() + "/lemeng/";
        String fileUrl = sdpath + "download/";
        String uri = fileUrl + listBean.getSongid() + ".mp3";
        File file = new File(uri);
        if ((listBean.isDownload() || listBean1.size() > 0) && file.exists()) {
            if (listBean.isPlaying()) {
                holder.go_player.setText(mContext.getResources().getString(R.string.pause));
                holder.go_player.setTextColor(mContext.getResources().getColor(R.color.main));
                holder.go_player.setBackground(mContext.getResources().getDrawable(R.drawable.download_bg2));
            } else if (listBean.isPause()) {
                holder.go_player.setText(mContext.getResources().getString(R.string.go_on));
                holder.go_player.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.go_player.setBackground(mContext.getResources().getDrawable(R.drawable.download_bg3));
            } else {
                holder.go_player.setText(mContext.getResources().getString(R.string.play));
                holder.go_player.setTextColor(mContext.getResources().getColor(R.color.main_66));
                holder.go_player.setBackground(mContext.getResources().getDrawable(R.drawable.download_bg));
            }
        } else {
            holder.go_player.setText(mContext.getResources().getString(R.string.download));
            holder.go_player.setBackground(mContext.getResources().getDrawable(R.drawable.download_bg1));
            holder.go_player.setTextColor(mContext.getResources().getColor(R.color.gray));
        }
        holder.audio_author.setText(listBean.getArtistname());
        holder.audio_title.setText(listBean.getSongname());
        holder.go_player.setTag(position);
        holder.parent_item.setTag(position);
        holder.go_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "";
                if (holder.go_player.getText().toString().equals(mContext.getResources().getString(R.string.play))) {
                    str = mContext.getResources().getString(R.string.play);
                } else  if (holder.go_player.getText().toString().equals(mContext.getResources().getString(R.string.pause))) {
                    str = mContext.getResources().getString(R.string.pause);
                } else  if (holder.go_player.getText().toString().equals(mContext.getResources().getString(R.string.go_on))) {
                    str = mContext.getResources().getString(R.string.go_on);
                } else {
                    str = mContext.getResources().getString(R.string.download);
                }
                if (onAudioClickListener != null)
                    onAudioClickListener.OnAudioItemClick((int) v.getTag(), str);
            }
        });

        holder.parent_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String str = "";
                if (holder.go_player.getText().toString().equals(mContext.getResources().getString(R.string.play))) {
                    str = mContext.getResources().getString(R.string.play);
                } else  if (holder.go_player.getText().toString().equals(mContext.getResources().getString(R.string.pause))) {
                    str = mContext.getResources().getString(R.string.pause);
                } else  if (holder.go_player.getText().toString().equals(mContext.getResources().getString(R.string.go_on))) {
                    str = mContext.getResources().getString(R.string.go_on);
                } else {
                    str = mContext.getResources().getString(R.string.download);
                }
                if (onAudioClickListener != null)
                    onAudioClickListener.OnAudioLongItemClick((int) v.getTag(), str);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    class AudioPlayerViewHolder extends RecyclerView.ViewHolder {
        TextView audio_title, audio_author, go_player;
        LinearLayout parent_item;

        public AudioPlayerViewHolder(View itemView) {
            super(itemView);
            audio_title = (TextView) itemView.findViewById(R.id.audio_title);
            audio_author = (TextView) itemView.findViewById(R.id.audio_author);
            go_player = (TextView) itemView.findViewById(R.id.go_player);
            parent_item = (LinearLayout) itemView.findViewById(R.id.parent_item);
        }
    }

    public interface OnAudioClickListener {
        void OnAudioItemClick(int position, String str);

        void OnAudioLongItemClick(int position, String str);

    }

    public void setOnAudioClickListener(OnAudioClickListener onAudioClickListener) {
        this.onAudioClickListener = onAudioClickListener;
    }

}
