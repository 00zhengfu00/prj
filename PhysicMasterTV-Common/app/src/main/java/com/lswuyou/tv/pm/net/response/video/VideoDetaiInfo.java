package com.lswuyou.tv.pm.net.response.video;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/18.
 */
public class VideoDetaiInfo implements Serializable {
    public int canPlay;//是否能播放
    public String introduction;//视频介绍
    public String making;   //策划人
    public String planning;   //制作人
    public String posterUrl;    //海报
    public int videoId;
    public int isFav;//1:已经收藏    0:未收藏
    public String title;
    public int tvVideoType;//电视平台视频类型：1：知识点解析；3：基础经典习题；4：夯实经典习题；5：提高经典习题
    public int isVip;
    public long playProgress = 0L;
}
