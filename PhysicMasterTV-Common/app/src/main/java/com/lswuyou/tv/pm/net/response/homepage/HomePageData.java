package com.lswuyou.tv.pm.net.response.homepage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class HomePageData implements Serializable{
    // 专题课程列表，如果ztCourseList只有5个元素，则第一个元素的图片的宽高比=16:19，否则都是16:9
    public List<CourseInfo> ztCourseList;
    // 精选视频列表
    public List<VideoInfo> jxVideoList;
    // 精选课程列表
    public List<CourseInfo> jxCourseList;
    //1表示第一个精选视频占两行 0 表示占一行
    public int isJxFirstTwoRow;
    // 打包视频集合
    public List<CoursePackInfo> tapPackCourseList;
}
