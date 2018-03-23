package com.lswuyou.tv.pm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.net.response.homepage.ChapterListResponse.DataBean.BookListBean.ItemListBean;

import java.util.List;

import reco.frame.tv.view.component.TvBaseAdapter;

public class TvCommonGridAdapter extends TvBaseAdapter {
    private List<ItemListBean> appList;
    private LayoutInflater inflater;
    private Context mContext;

    public TvCommonGridAdapter(Context context, List<ItemListBean> appList) {
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
        ItemListBean courseInfo = appList.get(position);
        Glide.with(mContext).load(courseInfo.poster).placeholder(R.mipmap.loading) //设置占位图
                .error(R.mipmap.loading).into(holder.tiv_icon);
        holder.tv_title.setText(courseInfo.title);
        return contentView;
    }

    public void addItem(ItemListBean item) {
        appList.add(item);
    }

    public void clear() {
        appList.clear();
    }

    public void flush(List<ItemListBean> appListNew) {
        appList = appListNew;
    }


    static class ViewHolder {
        TextView tv_title;
        ImageView tiv_icon;
    }
}
