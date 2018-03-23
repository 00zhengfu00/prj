package com.lswuyou.tv.pm.net.response.course;

import com.lswuyou.tv.pm.net.response.homepage.VideoInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 */
public class CourseDetailVo implements Serializable{
    public int alreadyBuy;
    public int courseId;
    public String title;
    public String poster;
    public int price;
    public String priceYuan;
    public String originalPriceYuan;
    public String textIntro;
    public List<VideoInfo> hsXtVideoList;
    public List<VideoInfo> jcXtVideoList;
    public List<VideoInfo> tgXtVideoList;
    public List<VideoInfo> zsdVideoList;
}
