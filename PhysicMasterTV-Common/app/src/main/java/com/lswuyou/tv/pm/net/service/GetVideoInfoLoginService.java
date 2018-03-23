package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.video.GetVideoInfoResponse;

/**
 * Created by Administrator on 2016/8/18.
 */
public class GetVideoInfoLoginService extends OpenApiDataServiceBase {

    public GetVideoInfoLoginService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_VIDEO_DETAIL_LOGIN;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetVideoInfoResponse.class;
    }
}
