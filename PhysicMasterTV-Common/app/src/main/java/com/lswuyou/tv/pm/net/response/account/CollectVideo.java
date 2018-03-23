package com.lswuyou.tv.pm.net.response.account;

import com.lswuyou.tv.pm.net.response.homepage.VideoInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class CollectVideo implements Serializable{
    public List<VideoInfo> hsXtVideoList;
    public List<VideoInfo> jcXtVideoList;
    public List<VideoInfo> tgXtVideoList;
    public List<VideoInfo> zsdVideoList;
}
