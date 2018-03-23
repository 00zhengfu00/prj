package com.lswuyou.tv.pm.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.activity.ChapterDetailActivity;
import com.lswuyou.tv.pm.activity.CoursePackDetailActivity;
import com.lswuyou.tv.pm.adapter.TvCourseGridAdapter;
import com.lswuyou.tv.pm.net.response.homepage.CourseInfo;

import java.util.List;

import reco.frame.tv.view.TvHorizontalGridView;

/**
 * Created by Administrator on 2016/7/25.
 */
public class BnjsFragment extends BaseIndexFragment {
    private TvHorizontalGridView tgv_list;
    private TvCourseGridAdapter adapter;
    public List<CourseInfo> courseByGradeList;
    //课程年级，实际以课程ID来区分
    private int gradeCourseId = -1;
    private boolean hasHeader = false;
    private LocalBroadcastManager localBroadcastManager;
    private boolean forbidLastItemFocusRight = false;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_course_grade;
    }

    @Override
    protected void initView() {
        tgv_list = (TvHorizontalGridView) rootView.findViewById(R.id.tgv_list);
        load();
        localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.registerReceiver(gradePackBoughtReceiver, new IntentFilter
                (COURSE_LIST_UPDATE));
    }

    public int getGradeCourseId() {
        return gradeCourseId;
    }

    public void setGradeCourseId(int gradeCourseId) {
        this.gradeCourseId = gradeCourseId;
        hasHeader = true;
    }

    private void load() {
        //grade不为-1表示用户未登录或者未购买年级课程包
        if (hasHeader) {
            ImageView header = new ImageView(getActivity());
            header.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(getActivity()).load(R.mipmap.grade_pack).placeholder(R.mipmap.loading)
                    .error(R.mipmap.loading).into(header);
            tgv_list.addHeader(header);
        }
        adapter = new TvCourseGridAdapter(getActivity(), courseByGradeList);
        tgv_list.setAdapter(adapter);

        tgv_list.setOnItemSelectListener(new TvHorizontalGridView.OnItemSelectListener() {
            @Override
            public void onItemSelect(View item, int position) {
            }
        });
        tgv_list.setOnItemClickListener(new TvHorizontalGridView.OnItemClickListener() {

            @Override
            public void onItemClick(View item, int position) {
//                Object obj = CacheManager.getObject(CacheManager.TYPE_USER_INFO, CacheKeys
//                        .USERINFO_LOGINVO, LoginUserInfo.class);
//                if (obj != null) {
                CourseInfo info = courseByGradeList.get(position);
                if (info.isCoursePack == 1) {
                    Intent intent = new Intent(getActivity(), CoursePackDetailActivity.class);
                    intent.putExtra("courseId", info.chapterId);
                    intent.putExtra("title", info.title);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), ChapterDetailActivity.class);
                    intent.putExtra("courseId", info.chapterId);
                    intent.putExtra("title", info.title);
                    startActivity(intent);
                }
//                } else {
//                    LoginDialog dialog = new LoginDialog(getActivity(), new LoginDialog
//                            .OnLoginSuccessListener() {
//                        @Override
//                        public void onLoginSuccess() {
//                            getActivity().sendBroadcast(new Intent(UserFragment.USERINFO_UPDATE));
//                        }
//                    });
//                    dialog.show();
//                }
            }
        });
        tgv_list.setLastItemRightFocusOutFibidden(forbidLastItemFocusRight);
        tgv_list.setOnHeaderClickListener(new TvHorizontalGridView.OnHeaderClickListener() {
            @Override
            public void onItemClick(View item) {
                Intent intent = new Intent(getActivity(), CoursePackDetailActivity.class);
                intent.putExtra("courseId", gradeCourseId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (gradePackBoughtReceiver != null) {
            localBroadcastManager.unregisterReceiver(gradePackBoughtReceiver);
        }
    }

    public void setCourseByGradeList(List<CourseInfo> courseByGradeList) {
        this.courseByGradeList = courseByGradeList;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setInitFocus(int index) {
        if (index == 0) {
            tgv_list.setInitFocus(0);
            tgv_list.resetLocation();
        } else if (index == 1) {
            int size = courseByGradeList.size();
            if (!hasHeader) {
                if (size % 2 == 0) {
                    index = size - 2;
                } else {
                    index = size - 1;
                }
            } else {
                if (size % 2 == 0) {
                    index = size - 1;
                } else {
                    index = size - 2;
                }
            }
            tgv_list.setInitFocus(index);
            tgv_list.scrollToLastPage();
        }
    }

    @Override
    public void reset() {
        tgv_list.resetLocation();
    }

    @Override
    public void setSavedFocus() {
        tgv_list.setSavedFocus();
    }

    /**
     * 设置grideview 最后一个item焦点是否右移出
     *
     * @param bool
     */

    public void setForbidLastItemFocusRight(boolean bool) {
        forbidLastItemFocusRight = bool;
    }

    /**
     * 年级课程包已购买监听
     */
    private BroadcastReceiver gradePackBoughtReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int courseId = intent.getIntExtra("courseId", -1);
            boolean add = intent.getBooleanExtra("add", false);
            if (courseId == gradeCourseId) {
                //根据标志add判断增加header或者删除header
                if (add) {
                    tgv_list.setHeader(createHeader());
                    hasHeader = true;
                } else {
                    tgv_list.removeHeader();
                    hasHeader = false;
                }
            }
        }
    };

    /**
     * 创建header
     */
    private View createHeader() {
        ImageView header = new ImageView(getActivity());
        header.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(getActivity()).load(R.mipmap.grade_pack).placeholder(R.mipmap.loading)
                .error(R.mipmap.loading).into(header);
        return header;
    }
}
