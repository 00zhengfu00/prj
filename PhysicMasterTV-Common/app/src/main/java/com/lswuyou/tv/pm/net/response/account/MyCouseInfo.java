package com.lswuyou.tv.pm.net.response.account;

import com.lswuyou.tv.pm.net.response.homepage.CourseInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class MyCouseInfo implements Serializable{
    public List<StudiedCourseInfo> courseList;
    public int latestStudyDays;
    public int studyCourseCountThisMonth;
}
