package com.lswuyou.tv.pm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.net.response.account.OrderBean;
import com.lswuyou.tv.pm.net.response.course.CourseSimpleInfo;

import java.util.List;

import reco.frame.tv.view.component.TvBaseAdapter;

public class TvMyCourseListAdapter extends TvBaseAdapter {
    private List<CourseSimpleInfo> courses;
    private LayoutInflater inflater;

    public TvMyCourseListAdapter(Context context, List<CourseSimpleInfo> appList) {
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
        CourseSimpleInfo order = courses.get(position);
        holder.tvCourse.setText(courses.get(position).title);
        return contentView;
    }

    public void addItem(CourseSimpleInfo item) {
        courses.add(item);
    }

    public void clear() {
        courses.clear();
    }

    public void flush(List<CourseSimpleInfo> appListNew) {
        courses = appListNew;
    }


    static class ViewHolder {
        TextView tvCourse;
    }
}
