package com.lswuyou.tv.pm.fragment;

import android.content.Intent;
import android.view.View;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.activity.ChapterDetailActivity;
import com.lswuyou.tv.pm.adapter.TvMyCourseGridAdapter;
import com.lswuyou.tv.pm.net.response.course.CourseSimpleInfo;

import java.util.List;

import reco.frame.tv.view.TvGridView;

/**
 * Created by Administrator on 2016/8/23.
 */
public class MyCourseFragment extends BaseFragment {
    private TvGridView tgv_list;
    private TvMyCourseGridAdapter adapter;
    private List<CourseSimpleInfo> courses;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_course_video;
    }

    @Override
    protected void initView() {
        tgv_list = (TvGridView) rootView.findViewById(R.id.tgv_imagelist);
        load();
    }

    private void load() {
        if (courses == null || courses.size() == 0) {
            return;
        }

        adapter = new TvMyCourseGridAdapter(getActivity(), courses);
        tgv_list.setAdapter(adapter);

        tgv_list.setOnItemSelectListener(new TvGridView.OnItemSelectListener() {

            @Override
            public void onItemSelect(View item, int position) {
            }
        });

        tgv_list.setOnItemClickListener(new TvGridView.OnItemClickListener() {

            @Override
            public void onItemClick(View item, int position) {
                Intent intent = new Intent(getActivity(), ChapterDetailActivity.class);
                intent.putExtra("courseId",courses.get(position).courseId);
                intent.putExtra("title",courses.get(position).title);
                startActivity(intent);
            }
        });
    }

    public void setCourses(List<CourseSimpleInfo> courses) {
        this.courses = courses;
    }
}
