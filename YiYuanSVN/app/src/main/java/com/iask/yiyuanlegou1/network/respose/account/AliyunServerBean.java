package com.iask.yiyuanlegou1.network.respose.account;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/2.
 */
public class AliyunServerBean implements Serializable {
    private String bucketName;
    private String cdnName;
    private String imgPath;
    // 默认的行政区划JSON数据文件URL
    private String regionUrl;

    public String getRegionUrl() {
        return regionUrl;
    }

    public void setRegionUrl(String regionUrl) {
        this.regionUrl = regionUrl;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getCdnName() {
        return cdnName;
    }

    public void setCdnName(String cdnName) {
        this.cdnName = cdnName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
