package com.lswuyou.tv.pm.net.response.video;

/**
 * Created by Administrator on 2016/8/19.
 */
public class VideoPlayInfo {
    public String title;
    public int canPlay;//1:可以播放   0:不可播放   当不可以播放时  下面是都没值的
    public int tvVideoType;
    public int isFav;//视频是否收藏　1收藏　　　0未收藏　　　用户未登录默认为0
    public String m3u8Content;
    public int videoId;
}
