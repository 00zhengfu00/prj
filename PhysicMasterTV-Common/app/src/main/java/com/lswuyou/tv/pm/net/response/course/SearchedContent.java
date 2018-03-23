package com.lswuyou.tv.pm.net.response.course;

import com.lswuyou.tv.pm.net.response.homepage.CourseInfo;
import com.lswuyou.tv.pm.net.response.homepage.VideoInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/8/25.
 */
public class SearchedContent {
    //匹配到的课程
    public List<CourseInfo> courseListVos;
    //匹配到的视频
    public List<VideoInfo> videoListVos;
}
