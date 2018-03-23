package com.lswuyou.tv.pm.net.response.homepage;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/15.
 */
public class VideoInfo extends CommonInfo implements Serializable {
    public int tvVideoType; // 电视平台视频类型：1：知识点解析；3：基础经典习题；4：夯实基础习题；5：提高经典习题
    public int videoId;// 视频ID
    public int isVip;
    public int playAllow;// 这个视频是否可以免费播放，这个只是这个视频本身的属性，与用户是否购买无关；0：付费视频；1：免费视频；
}
