package com.lswuyou.tv.pm.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.adapter.TvBoughtCourseGridAdapter;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.response.course.GetPackCourseResponse;
import com.lswuyou.tv.pm.net.response.homepage.CourseInfo;
import com.lswuyou.tv.pm.net.service.GetPackCourseLoginedService;
import com.lswuyou.tv.pm.view.TitleBarView;

import java.util.List;

import reco.frame.tv.view.TvGridView;

public class CoursePackBoughtActivity extends BaseActivity {
    private TitleBarView mTitleBarView;
    private TvBoughtCourseGridAdapter adapter;
    private TvGridView tgv_list;
    private List<CourseInfo> courses;

    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView) findViewById(R.id.title);
    }

    @Override
    protected void initView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
        int courseId = getIntent().getIntExtra("courseId", 0);
        mTitleBarView.setBtnLeft(0, R.string.zhuangti);
        tgv_list = (TvGridView) findViewById(R.id.tgv_imagelist);
        String title = getIntent().getStringExtra("title");
        ((TextView) findViewById(R.id.tv_course_name)).setText(title);
        getCoursePackInfo(courseId);
    }

    //获取课程包详情
    private void getCoursePackInfo(int courseId) {
        GetPackCourseLoginedService service = new GetPackCourseLoginedService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetPackCourseResponse>() {
            @Override
            public void onGetData(GetPackCourseResponse data) {
                try {
                    courses = data.data.packCourseVo.subCourseList;
                    load();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(CoursePackBoughtActivity.this, "数据异常！", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(CoursePackBoughtActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.postAES("courseId=" + courseId, false);
    }

    private void load() {
        if (courses == null || courses.size() == 0) {
            return;
        }

        adapter = new TvBoughtCourseGridAdapter(this, courses);
        tgv_list.setAdapter(adapter);

        tgv_list.setOnItemSelectListener(new TvGridView.OnItemSelectListener() {

            @Override
            public void onItemSelect(View item, int position) {
            }

        });

        tgv_list.setOnItemClickListener(new TvGridView.OnItemClickListener() {

            @Override
            public void onItemClick(View item, int position) {
                Intent intent = new Intent(CoursePackBoughtActivity.this, ChapterDetailActivity
                        .class);
                intent.putExtra("chapterId", courses.get(position).chapterId);
                intent.putExtra("title", courses.get(position).title);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_course_pack_info;
    }
}
