//package com.lswuyou.tv.pm.fragment;
//
//import android.content.Intent;
//import android.view.View;
//
//import com.lswuyou.tv.pm.R;
//import com.lswuyou.tv.pm.activity.CourseDetailActivity;
//import com.lswuyou.tv.pm.activity.CoursePackDetailActivity;
//import com.lswuyou.tv.pm.activity.MainActivity;
//import com.lswuyou.tv.pm.adapter.TvVideoGridAdapter;
//import com.lswuyou.tv.pm.net.response.homepage.HomePageData;
//import com.lswuyou.tv.pm.net.response.homepage.VideoInfo;
//
//import java.util.List;
//
//import reco.frame.tv.view.TvHorizontalGridView;
//
///**
// * Created by Administrator on 2016/7/25.
// */
//public class CourseFragment extends BaseFragment {
//    private TvHorizontalGridView tgv_list;
//    private TvVideoGridAdapter adapter;
//    // 精选视频列表
//    public List<VideoInfo> jxVideoList;
//
//    @Override
//    protected int getContentLayout() {
//        return R.layout.fragment_course;
//    }
//
//    @Override
//    protected void initView() {
//        tgv_list = (TvHorizontalGridView) rootView.findViewById(R.id.tgv_list);
//    }
//
//    private void load() {
//
//        adapter = new TvVideoGridAdapter(getActivity(), jxVideoList);
//        tgv_list.setAdapter(adapter);
//
//
//        tgv_list.setOnItemSelectListener(new TvHorizontalGridView.OnItemSelectListener() {
//
//            @Override
//            public void onItemSelect(View item, int position) {
//
//            }
//        });
//
//        tgv_list.setOnItemClickListener(new TvHorizontalGridView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(View item, int position) {
//                if (0 == position) {
//                    startActivity(new Intent(getActivity(), CourseDetailActivity.class));
//                } else {
//                    startActivity(new Intent(getActivity(), CoursePackDetailActivity.class));
//                }
//            }
//        });
//
//    }
//}
