package com.physicmaster.modules.videoplay.cache;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.physicmaster.R;
import com.physicmaster.utils.FileSizeUtil;

import java.util.List;

public class VideoDownloadedAdapter extends BaseAdapter {
    private List<VideoInfoForShow> list;
    private Context mContext;

    public VideoDownloadedAdapter(List<VideoInfoForShow> list, Context mContext) {
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
                            .video_downloaded_info_item,
                    parent, false);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.video_title);
            holder.ivPoster = (ImageView) convertView.findViewById(R.id.iv_course_poster);
            holder.cbSelect = (CheckBox) convertView.findViewById(R.id.cb_select);
            holder.tvVideoSize = (TextView) convertView.findViewById(R.id.tv_video_size);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        VideoInfoForShow videoInfoForShow = list.get(position);
        holder.tvTitle.setText(list.get(position).getName());
        holder.tvVideoSize.setText("文件大小：" + FileSizeUtil.FormetFileSize(list.get(position)
                .getDownloadedSize(), FileSizeUtil.SIZETYPE_MB) + "M");
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
        return convertView;
    }

    class ViewHolder {
        public TextView tvTitle;
        public CheckBox cbSelect;
        public TextView tvVideoSize;
        public ImageView ivPoster;
    }


}
