package com.physicmaster.net.service.video;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.course.VideoDownloadInfoResponse;

/**
 * Created by huashigen on 2017/3/23.
 */

public class VideoDownloadInfoService extends OpenApiDataServiceBase {
    public VideoDownloadInfoService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.VIDEO_DOWNLOAD;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return VideoDownloadInfoResponse.class;
    }
}
