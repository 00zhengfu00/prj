package com.lswuyou.tv.pm.fragment;

import android.content.Intent;
import android.view.View;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.activity.VideoIntroActivity;
import com.lswuyou.tv.pm.activity.VideoIntroForExcActivity;
import com.lswuyou.tv.pm.adapter.TvVideoGridAdapter2;
import com.lswuyou.tv.pm.net.response.video.VideoDetaiInfo;

import java.util.List;

import reco.frame.tv.view.TvGridView;

/**
 * Created by Administrator on 2016/8/4.
 * 课程视频
 */
public class CourseVideoFragment2 extends BaseFragment {
    private TvGridView tgv_imagelist;
    private TvVideoGridAdapter2 adapter;
    private List<VideoDetaiInfo> videoList;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_course_video;
    }

    @Override
    protected void initView() {
        tgv_imagelist = (TvGridView) rootView.findViewById(R.id.tgv_imagelist);
        load();
    }

    private void load() {
        if (videoList == null || videoList.size() == 0) {
            return;
        }
        adapter = new TvVideoGridAdapter2(getActivity(), videoList);
        tgv_imagelist.setAdapter(adapter);

        tgv_imagelist.setOnItemSelectListener(new TvGridView.OnItemSelectListener() {

            @Override
            public void onItemSelect(View item, int position) {

            }
        });

        tgv_imagelist.setOnItemClickListener(new TvGridView.OnItemClickListener() {

            @Override
            public void onItemClick(View item, int position) {
                Intent intent;
                if (videoList.get(position).tvVideoType == 1) {
                    intent = new Intent(getActivity(), VideoIntroActivity.class);
                } else {
                    intent = new Intent(getActivity(), VideoIntroForExcActivity.class);
                }
                intent.putExtra("videoId", videoList.get(position).videoId);
                startActivity(intent);
            }
        });
    }

    public void setVideoList(List<VideoDetaiInfo> videoList) {
        this.videoList = videoList;
    }
}
