package com.lswuyou.tv.pm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.net.response.homepage.VideoInfo;

import java.util.List;

import reco.frame.tv.view.TvImageView;
import reco.frame.tv.view.component.TvBaseAdapter;

public class TvKeyboardGridAdapter extends TvBaseAdapter {
    private String[] appList;
    private LayoutInflater inflater;
    private Context mContext;

    public TvKeyboardGridAdapter(Context context, String[] appList) {
        this.inflater = LayoutInflater.from(context);
        this.appList = appList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return appList.length;
    }

    @Override
    public Object getItem(int position) {
        return appList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder = null;
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.item_keyboard_grid, parent, false);
            holder = new ViewHolder();
            holder.tv_title = (TextView) contentView.findViewById(R.id.tv_title);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        holder.tv_title.setText(appList[position]);
        return contentView;
    }

    static class ViewHolder {
        TextView tv_title;
        TvImageView tiv_icon;
    }
}
