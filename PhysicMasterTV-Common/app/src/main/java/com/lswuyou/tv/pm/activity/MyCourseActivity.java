package com.lswuyou.tv.pm.activity;

import android.view.View;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.fragment.MyCourseFragment;
import com.lswuyou.tv.pm.net.response.account.CollectVideo;
import com.lswuyou.tv.pm.net.response.account.UserAccountInfo;
import com.lswuyou.tv.pm.net.response.course.CoursePackVo;
import com.lswuyou.tv.pm.net.response.course.CourseSimpleInfo;
import com.lswuyou.tv.pm.view.TitleBarView;
import com.lswuyou.tv.pm.view.TvVerticalTabHost2;

import java.util.ArrayList;
import java.util.List;

public class MyCourseActivity extends BaseFragmentActivity {

    private TitleBarView mTitleBarView;
    private List<CoursePackVo> myCourses;

    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView) findViewById(R.id.title);
    }

    @Override
    protected void initView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
        mTitleBarView.setBtnLeft(0, R.string.myCourse);
        UserAccountInfo userAccountInfo = (UserAccountInfo) getIntent().getSerializableExtra
                ("myCourse");
        myCourses = userAccountInfo.MyCoursePackVoList;
        loadFrag();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_my_course;
    }

    private void loadFrag() {
        /**
         * 添加页面
         */
        TvVerticalTabHost2 tth_container = (TvVerticalTabHost2) findViewById(R.id.tth_container);
        if (myCourses == null || 0 == myCourses.size()) {
            return;
        }
        for (int i = 0; i < myCourses.size(); i++) {
            MyCourseFragment fragment = new MyCourseFragment();
            List<CourseSimpleInfo> courseList1;
            if (null == myCourses.get(i).myCourseVos) {
                courseList1 = new ArrayList<>();
                CourseSimpleInfo info = new CourseSimpleInfo();
                info.courseId = myCourses.get(i).courseId;
                info.title = myCourses.get(i).title;
                courseList1.add(info);
                fragment.setCourses(courseList1);
            } else {
                fragment.setCourses(myCourses.get(i).myCourseVos);
            }
            tth_container.addPage(getFragmentManager(), fragment, myCourses.get(i).title,
                    true);
        }
        tth_container.buildLayout();

        /**
         * 设监听
         */
        tth_container.setOnPageChangeListener(new TvVerticalTabHost2.ScrollPageChangerListener() {

            @Override
            public void onPageSelected(int pageCurrent) {
            }
        });
        /**
         * 页面跳转
         */
        tth_container.setCurrentPage(0);
    }
}
