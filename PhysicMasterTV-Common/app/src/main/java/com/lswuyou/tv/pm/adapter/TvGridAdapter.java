package com.lswuyou.tv.pm.adapter;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.net.response.video.VideoDetaiInfo;

import java.util.List;

import reco.frame.tv.view.component.TvBaseAdapter;


public class TvGridAdapter extends TvBaseAdapter {
    private List<VideoDetaiInfo> appList;
    private LayoutInflater inflater;
    private Context mContext;
    private OnMenuClickListener onMenuClickListener;

    public TvGridAdapter(Context context, List<VideoDetaiInfo> appList) {
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
        this.appList = appList;
    }

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        this.onMenuClickListener = onMenuClickListener;
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

    public View getView(final int position, View contentView, ViewGroup parent) {
        ViewHolder holder = null;
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.item_grid, null);
            holder = new ViewHolder();
            holder.tv_title = (TextView) contentView.findViewById(R.id.tv_title);
            holder.iv_icon = (ImageView) contentView.findViewById(R.id.iv_icon);
            contentView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_MENU) {
                            if (onMenuClickListener != null) {
                                onMenuClickListener.onMenuClick(position);
                            }
                        }
                    }
                    return false;
                }
            });
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        VideoDetaiInfo app = appList.get(position);
        holder.tv_title.setText(app.title);
        Glide.with(mContext).load(app.posterUrl).placeholder(R.mipmap.loading).into(holder.iv_icon);
        return contentView;
    }

    public void addItem(VideoDetaiInfo item) {
        appList.add(item);
    }

    public void clear() {
        appList.clear();
    }

    public void flush(List<VideoDetaiInfo> appListNew) {
        appList = appListNew;
    }

    static class ViewHolder {
        TextView tv_title;
        ImageView iv_icon;
    }

    public interface OnMenuClickListener {
        public void onMenuClick(int position);
    }
}
