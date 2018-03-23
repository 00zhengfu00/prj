package com.lswuyou.tv.pm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.net.response.course.CourseSimpleInfo;
import com.lswuyou.tv.pm.net.response.homepage.CourseInfo;

import java.util.List;

import reco.frame.tv.view.component.TvBaseAdapter;

public class TvCoursesBoughtListAdapter extends TvBaseAdapter {
    private List<CourseInfo> courses;
    private LayoutInflater inflater;

    public TvCoursesBoughtListAdapter(Context context, List<CourseInfo> appList) {
        this.inflater = LayoutInflater.from(context);
        this.courses = appList;
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public Object getItem(int position) {
        return courses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder = null;
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.item_course_info, null);
            holder = new ViewHolder();
            holder.tvCourse = (TextView) contentView.findViewById(R.id.tv_course);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        CourseInfo order = courses.get(position);
        holder.tvCourse.setText(courses.get(position).title);
        return contentView;
    }

    public void addItem(CourseInfo item) {
        courses.add(item);
    }

    public void clear() {
        courses.clear();
    }

    public void flush(List<CourseInfo> appListNew) {
        courses = appListNew;
    }


    static class ViewHolder {
        TextView tvCourse;
    }
}
