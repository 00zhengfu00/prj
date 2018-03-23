package com.lswuyou.tv.pm.net.response.course;

import com.lswuyou.tv.pm.net.response.homepage.CourseInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class PackCourseBean implements Serializable{
    public int alreadyBuy;
    public int courseId;
    public int originalPrice;
    public int price;
    public String originalPriceYuan;
    public String priceYuan;
    public String title;
    public List<CourseInfo> subCourseList;
}
