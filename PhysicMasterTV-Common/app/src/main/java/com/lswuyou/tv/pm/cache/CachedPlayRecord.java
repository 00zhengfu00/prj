package com.lswuyou.tv.pm.cache;

import com.lswuyou.tv.pm.net.response.video.VideoDetaiInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
public class CachedPlayRecord implements Serializable {
    //1：知识点解析
    public List<VideoDetaiInfo> videoDetaiInfoList;
}
