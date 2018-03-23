package com.physicmaster.modules.videoplay.download;

import android.support.v4.util.ArrayMap;

import com.bumptech.glide.util.LogTime;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by huashigen on 2017-08-22.
 * 下载任务封装
 */

public class DownloadTask {

    public DownloadTask(String m3u8String, String videoId, List<String> tsUrls, String videoTitle, String userId) {
        this.m3u8String = m3u8String;
        this.videoId = videoId;
        this.userId = userId;
        this.curTotalTsNum = new AtomicInteger(tsUrls.size());
        this.videoTitle = videoTitle;
        tsUrlsMap = new ArrayMap<>();
        for (int i = 0; i < tsUrls.size(); i++) {
            String key = "###TS-" + i + "###";
            tsUrlsMap.put(key, tsUrls.get(i));
        }
    }

    public String m3u8String;
    public AtomicInteger curTotalTsNum;
    public String videoId;
    public String userId;
    public String videoTitle;
    public long totalSize = 0L;
    public ArrayMap<String, String> tsUrlsMap;

    /**
     * 将m3u8String的所有网络地址替换为本地地址
     */
    public void replaceM3u8String() {
        Iterator<String> keys = tsUrlsMap.keySet().iterator();
        String targetString;
        while (keys.hasNext()) {
            String key = keys.next();
            String value = tsUrlsMap.get(key);
            targetString = m3u8String.replace(key, value);
            m3u8String = new String(targetString);
        }
    }
}
