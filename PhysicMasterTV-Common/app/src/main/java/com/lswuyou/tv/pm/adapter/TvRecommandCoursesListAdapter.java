package com.lswuyou.tv.pm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.net.response.course.RecommandCourseVo;
import com.lswuyou.tv.pm.net.response.homepage.CourseInfo;

import java.util.List;

import reco.frame.tv.view.component.TvBaseAdapter;

public class TvRecommandCoursesListAdapter extends TvBaseAdapter {
    private List<RecommandCourseVo> appList;
    private LayoutInflater inflater;

    public TvRecommandCoursesListAdapter(Context context, List<RecommandCourseVo> appList) {
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
            contentView = inflater.inflate(R.layout.item_recommand_course, parent, false);
            holder = new ViewHolder();
            holder.tvCourse = (TextView) contentView.findViewById(R.id.tv_title);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        holder.tvCourse.setText(appList.get(position).title);
        return contentView;
    }

    static class ViewHolder {
        TextView tvCourse;
    }
}
