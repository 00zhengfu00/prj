package com.lswuyou.tv.pm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.net.response.homepage.CourseInfo;

import java.util.List;

import reco.frame.tv.view.component.TvBaseAdapter;

public class TvBoughtCourseGridAdapter extends TvBaseAdapter {
    private List<CourseInfo> appList;
    private LayoutInflater inflater;
    private Context mContext;
    private static int colors[] = {R.color.course_item_color1, R.color.course_item_color2, R.color
            .course_item_color3, R.color.course_item_color4};
    public TvBoughtCourseGridAdapter(Context context, List<CourseInfo> appList) {
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
            contentView = inflater.inflate(R.layout.item_bought, parent, false);
            holder = new ViewHolder();
            holder.tv_title = (TextView) contentView.findViewById(R.id.tv_title);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        int index = position%4;
        CourseInfo courseInfo = appList.get(position);
        holder.tv_title.setText(courseInfo.title);
        holder.tv_title.setBackgroundColor(mContext.getResources().getColor(colors[index]));
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
        TextView tv_title;
    }
}
