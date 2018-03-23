package com.iask.yiyuanlegou1.home.main.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.log.AndroidLogger;
import com.iask.yiyuanlegou1.log.Logger;
import com.iask.yiyuanlegou1.network.respose.product.AnnounceHistoryBean;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class HistoryRecordAdapter extends BaseAdapter {
    private List<AnnounceHistoryBean> data;
    private Context context;
    private Logger logger = AndroidLogger.getLogger(getClass().getSimpleName());

    public HistoryRecordAdapter(List<AnnounceHistoryBean> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.listview_history_record_item, parent, false);
//            holder.tvNickname = (TextView) convertView.findViewById(R.id.nickName);
            holder.tvParCount = (TextView) convertView
                    .findViewById(R.id.par_count);
            holder.tvCode = (TextView) convertView
                    .findViewById(R.id.lcode_num);
            holder.tvParAndTime = (TextView) convertView
                    .findViewById(R.id.announce_time);
            holder.tvTitle = (TextView) convertView
                    .findViewById(R.id.tv_title);
            holder.tvNper = (TextView) convertView
                    .findViewById(R.id.tv_nper);
            holder.ivPicUrl = (RoundedImageView) convertView.findViewById(R.id.join_record_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AnnounceHistoryBean bean = data.get(position);
        holder.tvCode.setText(bean.getCode() + "");
        holder.tvParCount.setText(bean.getPartakeTimes() + "");
        holder.tvParAndTime.setText(bean.getAnnounceTime());
        holder.tvNper.setText(bean.getNper()+"");
        holder.tvTitle.setText(bean.getTitle());
        Glide.with(context).load(bean.getPhoto()).into(holder.ivPicUrl);
        return convertView;
    }

    class ViewHolder {
        TextView tvParAndTime;
        TextView tvNper;
        TextView tvTitle;
        TextView tvCode;
        TextView tvParCount;
        ImageView ivPicUrl;
    }
}
