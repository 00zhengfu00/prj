package com.physicmaster.net.response.course;

import java.util.List;

/**
 * Created by huashigen on 2016/11/21.
 */
public class CourseIndexDataWraper {
    //专题课程
    public List<CourseVo> ztCourses;
    //模块课程
    public List<CourseVo> mkCourses;
    //学期课程
    public List<CourseVo> xqCourses;
    //章节课程
    public List<CourseVo> zjCourses;
    //轮播图
    public List<BannerVo> banner;
}
