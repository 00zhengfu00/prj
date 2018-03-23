package com.iask.yiyuanlegou1.home.main.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.log.AndroidLogger;
import com.iask.yiyuanlegou1.log.Logger;
import com.iask.yiyuanlegou1.network.service.product.JoinRecordBean;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class JoinRecordAdapter extends BaseAdapter {
    private List<JoinRecordBean> data;
    private Context context;
    private Logger logger = AndroidLogger.getLogger(getClass().getSimpleName());

    public JoinRecordAdapter(List<JoinRecordBean> data, Context context) {
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.listview_join_record_item, parent, false);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.nickName);
            holder.tvJoinNum = (TextView) convertView
                    .findViewById(R.id.par_count);
            holder.tvTime = (TextView) convertView
                    .findViewById(R.id.par_time);
            holder.ivHead = (RoundedImageView) convertView
                    .findViewById(R.id.join_record_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        JoinRecordBean bean = data.get(position);
        Glide.with(context).load(bean.getPhoto()).into(holder.ivHead);
        holder.tvTitle.setText(bean.getUserName());
        holder.tvTime.setText(bean.getTime());
        holder.tvJoinNum.setText("参与了" + bean.getNum() + "人次");
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvJoinNum;
        TextView tvTime;
        RoundedImageView ivHead;
    }
}
