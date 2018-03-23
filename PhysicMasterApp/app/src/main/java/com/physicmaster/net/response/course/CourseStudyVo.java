package com.physicmaster.net.response.course;

import java.util.List;

/**
 * Created by huashigen on 2016/11/26.
 */
public class CourseStudyVo {
    public int alreadyBuy;//0,    //是否已购买　　　０：没有　　１：以购买
    public int courseId;//125,　　
    public String courseTitle;//机械运动,
    public String posterUrl;//http;//img.thelper.cn/dt/c/1463455466941gqllozus.jpg,
    public String priceYuan;//0.10
    public List<VideoVo> previewVideos;
    public List<VideoVo> reviewVideos;
}
