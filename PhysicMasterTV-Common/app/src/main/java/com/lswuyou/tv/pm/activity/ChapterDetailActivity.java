package com.lswuyou.tv.pm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.fragment.CourseVideoFragment;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.response.account.LoginUserInfo;
import com.lswuyou.tv.pm.net.response.course.GetChapterResponse;
import com.lswuyou.tv.pm.net.service.GetChapterLoginedService;
import com.lswuyou.tv.pm.net.service.GetChapterUnLoginedService;
import com.lswuyou.tv.pm.view.TitleBarView;
import com.lswuyou.tv.pm.view.TvVerticalTabHost2;

public class ChapterDetailActivity extends BaseFragmentActivity {
    private TitleBarView mTitleBarView;
    private TvVerticalTabHost2 tth_container;
    private int chapterId;
    private String title;

    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView) findViewById(R.id.title);
    }

    @Override
    protected void initView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
        title = getIntent().getStringExtra("title");
        mTitleBarView.setBtnLeftStr(title);
        loadFrag();
        chapterId = getIntent().getIntExtra("chapterId", 0);
        getData(chapterId);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_course_detail;
    }

    private void loadFrag() {
        tth_container = (TvVerticalTabHost2) findViewById(R.id.tth_container);
        /**
         * 设监听
         */
        tth_container.setOnPageChangeListener(new TvVerticalTabHost2.ScrollPageChangerListener() {

            @Override
            public void onPageSelected(int pageCurrent) {
            }
        });
    }

    //获取普通课程详情
    private void getData(int courseId) {
        OpenApiDataServiceBase service = null;
        Object obj = CacheManager.getObject(CacheManager.TYPE_USER_INFO, CacheKeys
                .USERINFO_LOGINVO, LoginUserInfo.class);
        if (obj != null) {
            service = new GetChapterLoginedService(this);
        } else {
            service = new GetChapterUnLoginedService(this);
        }
        service.setCallback(new IOpenApiDataServiceCallback<GetChapterResponse>() {
            @Override
            public void onGetData(final GetChapterResponse data) {
                try {
                    ChapterDetailActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshUI(data.data.chapterDetails);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(ChapterDetailActivity.this, "获取课程包详情失败！", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        if (obj != null) {
            service.postAES("chapterId=" + courseId, false);
        } else {
            service.post("chapterId=" + courseId, false);
        }
    }

    //刷新界面数据
    private void refreshUI(GetChapterResponse.DataBean.ChapterDetailsBean chapterListVo) {
        if (chapterListVo.previewList != null && chapterListVo.previewList.size() > 0) {
            CourseVideoFragment videoFragment1 = new CourseVideoFragment();
            Bundle bundleVf = new Bundle();
            bundleVf.putSerializable("videoList", chapterListVo);
            bundleVf.putString("type", "yxXtVideoList");
            bundleVf.putInt("chapterId", chapterId);
            videoFragment1.setArguments(bundleVf);
            tth_container.addPage(getFragmentManager(), videoFragment1, "导入课预习", true);
        }
        if (chapterListVo.deepList != null && chapterListVo.deepList.size() > 0) {
            CourseVideoFragment videoFragment = new CourseVideoFragment();
            Bundle bundleVf = new Bundle();
            bundleVf.putSerializable("videoList", chapterListVo);
            bundleVf.putString("type", "deepList");
            bundleVf.putInt("chapterId", chapterId);
            videoFragment.setArguments(bundleVf);
            tth_container.addPage(getFragmentManager(), videoFragment, "知识点精讲", true);
        }
        if (chapterListVo.jichuList != null && chapterListVo.jichuList.size() > 0) {
            CourseVideoFragment videoFragment2 = new CourseVideoFragment();
            Bundle bundleVf = new Bundle();
            bundleVf.putSerializable("videoList", chapterListVo);
            bundleVf.putString("type", "jcXtVideoList");
            bundleVf.putInt("chapterId", chapterId);
            videoFragment2.setArguments(bundleVf);
            tth_container.addPage(getFragmentManager(), videoFragment2, "基础经典习题", true);
        }
        if (chapterListVo.hangshiList != null && chapterListVo.hangshiList.size() > 0) {
            CourseVideoFragment videoFragment3 = new CourseVideoFragment();
            Bundle bundleVf = new Bundle();
            bundleVf.putSerializable("videoList", chapterListVo);
            bundleVf.putString("type", "hsXtVideoList");
            bundleVf.putInt("chapterId", chapterId);
            videoFragment3.setArguments(bundleVf);
            tth_container.addPage(getFragmentManager(), videoFragment3, "夯实经典习题", true);
        }
        if (chapterListVo.tigaoList != null && chapterListVo.tigaoList.size() > 0) {
            CourseVideoFragment videoFragment4 = new CourseVideoFragment();
            Bundle bundleVf = new Bundle();
            bundleVf.putSerializable("videoList", chapterListVo);
            bundleVf.putString("type", "tgXtVideoList");
            bundleVf.putInt("chapterId", chapterId);
            videoFragment4.setArguments(bundleVf);
            tth_container.addPage(getFragmentManager(), videoFragment4, "提高经典习题", true);
        }
        tth_container.buildLayout();
        /**
         * 页面跳转
         */
        tth_container.setCurrentPage(0);
    }

    @Override
    protected void onDestroy() {
        tth_container = null;
        super.onDestroy();
    }
}
