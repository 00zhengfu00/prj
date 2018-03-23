package com.lswuyou.tv.pm.net.response.account;

import com.lswuyou.tv.pm.net.response.course.CoursePackVo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class UserAccountInfo implements Serializable{
    //历史订单
    public List<OrderBean> historyOrders;
    //收藏视频
    public CollectVideo favVideoVo;
    //我的课程
    public List<CoursePackVo> MyCoursePackVoList;
}
