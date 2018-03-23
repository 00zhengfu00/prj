package com.physicmaster.modules.videoplay.cache;


import com.physicmaster.modules.videoplay.cache.bean.VideoInfo;

/**
 * Created by huashigen on 2017/3/23.
 */

public class VideoInfoForShow extends VideoInfo {
    //0-隐藏;1-显示但未选中;2-显示且选中
    private int status = 0;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
