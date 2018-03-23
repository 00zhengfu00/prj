package com.physicmaster.modules.videoplay.cache;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;

import java.util.List;


public class VideoDownloadingAdapter extends BaseAdapter {
    private List<VideoInfoForShow> list;
    private Context mContext;

    public VideoDownloadingAdapter(List<VideoInfoForShow> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout
                            .video_downloading_info_item,
                    parent, false);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.video_title);
            holder.ivPoster = (ImageView) convertView.findViewById(R.id.iv_course_poster);
            holder.cbSelect = (CheckBox) convertView.findViewById(R.id.cb_select);
            holder.tvProgress = (TextView) convertView.findViewById(R.id.tv_progress);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progress_loading);
            holder.tvCacheState = (TextView) convertView.findViewById(R.id.tv_cache_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        VideoInfoForShow videoInfoForShow = list.get(position);
        holder.tvTitle.setText(list.get(position).getName());
        Glide.with(mContext).load(list.get(position).getPosterUrl()).into(holder.ivPoster);
        if (videoInfoForShow.getStatus() == 0) {
            holder.cbSelect.setVisibility(View.GONE);
            holder.cbSelect.setChecked(false);
        } else if (videoInfoForShow.getStatus() == 1) {
            holder.cbSelect.setVisibility(View.VISIBLE);
            holder.cbSelect.setChecked(false);
        } else {
            holder.cbSelect.setVisibility(View.VISIBLE);
            holder.cbSelect.setChecked(true);
        }
        if (videoInfoForShow.getProgress() == 0) {
            holder.tvCacheState.setText("等待缓存");
            holder.tvProgress.setVisibility(View.GONE);
            holder.progressBar.setProgress(0);
        } else {
            holder.tvProgress.setVisibility(View.VISIBLE);
            holder.tvCacheState.setText("正在缓存");
            int progress = videoInfoForShow.getProgress();
            holder.tvProgress.setText(progress + "%");
            holder.progressBar.setProgress(progress);
        }
        return convertView;
    }

    class ViewHolder {
        public TextView tvTitle;
        public CheckBox cbSelect;
        public TextView tvProgress;
        public ImageView ivPoster;
        public TextView tvCacheState;
        public ProgressBar progressBar;
    }
}
