package com.lswuyou.tv.pm.net.response.homepage;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class CoursePackInfo implements Serializable{
    public List<CourseInfo> subCourseList;
    public int alreadyBuy; // 当前用户是否购买这个优惠订购包
    public int originalPrice;  // 课程原价（所有子课程包的价格之和）（单位：分；int类型）
    public String originalPriceYuan;// 课程原价（所有子课程包的价格之和）（单位：元；字符串类型）
    public int courseId;// 课程ID
    public int price;// 课程价格（单位：分；int类型）
    public String priceYuan;// 课程价格（单位：元；字符串类型）
    public String title;
}
