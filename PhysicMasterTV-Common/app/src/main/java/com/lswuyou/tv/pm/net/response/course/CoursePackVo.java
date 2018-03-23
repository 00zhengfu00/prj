package com.lswuyou.tv.pm.net.response.course;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/23.
 */
public class CoursePackVo implements Serializable{
    public int courseId;
    public List<CourseSimpleInfo> myCourseVos;
    public String title;
}
