package com.lswuyou.tv.pm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.net.response.homepage.VideoInfo;

import java.util.List;

import reco.frame.tv.view.component.TvBaseAdapter;

public class TvMenuListAdapter extends TvBaseAdapter {
    private List<String> names;
    private LayoutInflater inflater;

    public TvMenuListAdapter(Context context, List<String> appList) {
        this.inflater = LayoutInflater.from(context);
        this.names = appList;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder = null;
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.item_menu, null);
            holder = new ViewHolder();
            holder.tvMenu = (TextView) contentView.findViewById(R.id.tv_name);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        holder.tvMenu.setText(names.get(position) + "");
        return contentView;
    }

    public void addItem(String item) {
        names.add(item);
    }

    public void clear() {
        names.clear();
    }

    public void flush(List<String> appListNew) {
        names = appListNew;
    }


    static class ViewHolder {
        TextView tvMenu;
    }
}
