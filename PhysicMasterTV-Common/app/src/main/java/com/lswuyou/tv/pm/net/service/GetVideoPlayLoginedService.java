package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.video.GetVideoPlayResponse;

/**
 * Created by Administrator on 2016/8/18.
 */
public class GetVideoPlayLoginedService extends OpenApiDataServiceBase {
    public GetVideoPlayLoginedService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_VIDEO_PLAy_LOGIN;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetVideoPlayResponse.class;
    }
}


