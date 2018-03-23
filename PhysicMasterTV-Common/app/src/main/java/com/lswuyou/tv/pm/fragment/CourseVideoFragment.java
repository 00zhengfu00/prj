package com.lswuyou.tv.pm.fragment;

import android.content.Intent;
import android.view.View;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.activity.VideoIntroActivity;
import com.lswuyou.tv.pm.activity.VideoIntroForExcActivity;
import com.lswuyou.tv.pm.adapter.TvVideoGridAdapter;
import com.lswuyou.tv.pm.databean.VideoDataWrapper;
import com.lswuyou.tv.pm.net.response.course.GetChapterResponse.DataBean.ChapterDetailsBean;
import com.lswuyou.tv.pm.net.response.homepage.VideoInfo;

import java.util.List;

import reco.frame.tv.view.TvGridView;

/**
 * Created by Administrator on 2016/8/4.
 * 课程视频
 */
public class CourseVideoFragment extends BaseFragment {

    private TvGridView tgv_imagelist;
    private TvVideoGridAdapter adapter;
    private List<VideoInfo> videoList;
    private int chapterId;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_course_video;
    }

    @Override
    protected void initView() {
        tgv_imagelist = (TvGridView) rootView.findViewById(R.id.tgv_imagelist);
        ChapterDetailsBean chapterVo = (ChapterDetailsBean) getArguments().getSerializable("videoList");
        String type = getArguments().getString("type");
        chapterId = getArguments().getInt("chapterId");
        if (type.equals("tgXtVideoList")) {
            videoList = chapterVo.tigaoList;
            if (videoList == null || videoList.size() == 0) {
                return;
            }
            for (VideoInfo videoInfo : videoList) {
                videoInfo.tvVideoType = 5;
            }
        } else if (type.equals("yxXtVideoList")) {
            videoList = chapterVo.previewList;
            if (videoList == null || videoList.size() == 0) {
                return;
            }
            for (VideoInfo videoInfo : videoList) {
                videoInfo.tvVideoType = 1;
            }
        } else if (type.equals("hsXtVideoList")) {
            videoList = chapterVo.hangshiList;
            if (videoList == null || videoList.size() == 0) {
                return;
            }
            for (VideoInfo videoInfo : videoList) {
                videoInfo.tvVideoType = 4;
            }
        } else if (type.equals("deepList")) {
            videoList = chapterVo.deepList;
            if (videoList == null || videoList.size() == 0) {
                return;
            }
            for (VideoInfo videoInfo : videoList) {
                videoInfo.tvVideoType = 1;
            }
        } else {
            videoList = chapterVo.jichuList;
            if (videoList == null || videoList.size() == 0) {
                return;
            }
            for (VideoInfo videoInfo : videoList) {
                videoInfo.tvVideoType = 3;
            }
        }
        load();
    }

    private void load() {
        adapter = new TvVideoGridAdapter(getActivity(), videoList);
        tgv_imagelist.setAdapter(adapter);
        tgv_imagelist.setOnItemSelectListener(new TvGridView.OnItemSelectListener() {
            @Override
            public void onItemSelect(View item, int position) {
            }
        });
        tgv_imagelist.setOnItemClickListener(new TvGridView.OnItemClickListener() {
            @Override
            public void onItemClick(View item, int position) {
                Intent intent = null;
                VideoInfo videoInfo = videoList.get(position);
                if (videoInfo.tvVideoType == 1) {
                    intent = new Intent(getActivity(), VideoIntroActivity.class);
                } else {
                    intent = new Intent(getActivity(), VideoIntroForExcActivity.class);
                }
                intent.putExtra("videoId", videoList.get(position).videoId);
                intent.putExtra("chapterId", chapterId);
                intent.putExtra("index", position);
                VideoDataWrapper videoDataWrapper = new VideoDataWrapper();
                videoDataWrapper.videoList = videoList;
                intent.putExtra("videoList", videoDataWrapper);
                startActivity(intent);
            }
        });
    }
}
