package com.lswuyou.tv.pm.adapter;

/**
 * Created by Administrator on 2016/7/25.
 */
public class CommonGridItem {
    public String poster;// 课程海报
    public int price;// 课程价格（单位：分；int类型）
    public String priceYuan;// 课程价格（单位：元；字符串类型）
    public String title;  // 课程标题
    public int alreadyBuy;
    public int type;//0-课程包;1-课程;2-视频
    public int tvVideoType;// 电视平台视频类型：1：知识点解析；3：基础经典习题；4：夯实基础习题；5：提高经典习题
    public int id;
}
