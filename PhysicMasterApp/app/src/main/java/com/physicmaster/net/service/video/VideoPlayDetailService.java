package com.physicmaster.net.service.video;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.course.VideoPlayResponse;

/**
 * Created by huashigen on 2016/11/26.
 */
public class VideoPlayDetailService extends OpenApiDataServiceBase {
    public VideoPlayDetailService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.VIDEO_PLAY;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return VideoPlayResponse.class;
    }
}
