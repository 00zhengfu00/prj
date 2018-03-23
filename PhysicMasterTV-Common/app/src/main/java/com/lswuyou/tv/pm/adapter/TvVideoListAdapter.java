package com.lswuyou.tv.pm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.net.response.account.OrderBean;
import com.lswuyou.tv.pm.net.response.homepage.VideoInfo;

import java.util.List;

import reco.frame.tv.view.component.TvBaseAdapter;

public class TvVideoListAdapter extends TvBaseAdapter {
    private List<VideoInfo> orders;
    private LayoutInflater inflater;

    public TvVideoListAdapter(Context context, List<VideoInfo> appList) {
        this.inflater = LayoutInflater.from(context);
        this.orders = appList;
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder = null;
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.item_video, null);
            holder = new ViewHolder();
            holder.tvVideoOrder = (TextView) contentView.findViewById(R.id.tv_video_num);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        holder.tvVideoOrder.setText(position + "");
        return contentView;
    }

    public void addItem(VideoInfo item) {
        orders.add(item);
    }

    public void clear() {
        orders.clear();
    }

    public void flush(List<VideoInfo> appListNew) {
        orders = appListNew;
    }


    static class ViewHolder {
        TextView tvVideoOrder;
    }
}
