package com.lswuyou.tv.pm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.net.response.homepage.CourseInfo;

import java.util.List;

import reco.frame.tv.view.component.TvBaseAdapter;

public class TvChapterListAdapter extends TvBaseAdapter {
    private List<CourseInfo> appList;
    private LayoutInflater inflater;

    public TvChapterListAdapter(Context context, List<CourseInfo> appList) {
        this.inflater = LayoutInflater.from(context);
        this.appList = appList;
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
            contentView = inflater.inflate(R.layout.item_chapter_list, parent, false);
            holder = new ViewHolder();
            holder.tvChapter = (TextView) contentView.findViewById(R.id.tv_chapter);
            holder.tvPrice = (TextView) contentView.findViewById(R.id.tv_price);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        holder.tvChapter.setText(appList.get(position).title);
        holder.tvPrice.setText(appList.get(position).priceYuan);
        return contentView;
    }

    public void addItem(CourseInfo item) {
        appList.add(item);
    }

    public void clear() {
        appList.clear();
    }

    public void flush(List<CourseInfo> appListNew) {
        appList = appListNew;
    }


    static class ViewHolder {
        TextView tvChapter;
        TextView tvPrice;
    }
}
