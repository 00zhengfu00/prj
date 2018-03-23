package com.lswuyou.tv.pm.net.response.homepage;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/15.
 */
public class CourseInfo extends CommonInfo implements Serializable{
    public int isCoursePack;// 是否是优惠课程包
    public int price;// 课程价格（单位：分；int类型）
    public String priceYuan;// 课程价格（单位：元；字符串类型）
    public String textIntro;
    public int alreadyBuy;
    public String originalPriceYuan;//原价
}
