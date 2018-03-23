package com.physicmaster.net.service.video;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.video.LikeVideoPlayResponse;

/**
 * Created by huashigen on 2017-07-25.
 */

public class LikeVideoPlayService extends OpenApiDataServiceBase {

    public LikeVideoPlayService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.LIKE_VIDEO_PLAY;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return LikeVideoPlayResponse.class;
    }
}
