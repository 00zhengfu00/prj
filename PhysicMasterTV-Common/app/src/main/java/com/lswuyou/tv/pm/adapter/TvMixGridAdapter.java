package com.lswuyou.tv.pm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.net.response.homepage.CommonInfo;
import com.lswuyou.tv.pm.net.response.homepage.VideoInfo;

import java.util.List;

import reco.frame.tv.view.component.TvBaseAdapter;

public class TvMixGridAdapter extends TvBaseAdapter {
    private List<CommonInfo> appList;
    private LayoutInflater inflater;
    private Context mContext;

    public TvMixGridAdapter(Context context, List<CommonInfo> appList) {
        this.inflater = LayoutInflater.from(context);
        this.appList = appList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public Object getItem(int position) {
        return appList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder = null;
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.item_grid, parent, false);
            holder = new ViewHolder();
            holder.tv_title = (TextView) contentView.findViewById(R.id.tv_title);
            holder.tiv_icon = (ImageView) contentView.findViewById(R.id.iv_icon);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        CommonInfo info = appList.get(position);
        holder.tv_title.setText(info.title);
        Glide.with(mContext).load(info.posterUrl).placeholder(R.mipmap.loading) //设置占位图
                .error(R.mipmap.loading).into(holder.tiv_icon);
        return contentView;
    }

    public void addItem(VideoInfo item) {
        appList.add(item);
    }

    public void clear() {
        appList.clear();
    }

    public void flush(List<CommonInfo> appListNew) {
        appList = appListNew;
    }


    static class ViewHolder {
        TextView tv_title;
        ImageView tiv_icon;
    }
}
