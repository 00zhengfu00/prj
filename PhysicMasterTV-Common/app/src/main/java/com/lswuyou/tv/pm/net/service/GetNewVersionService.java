package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.version.GetNewVersionResponse;

/**
 * Created by Administrator on 2016/8/22.
 */
public class GetNewVersionService extends OpenApiDataServiceBase {
    public GetNewVersionService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_NEW_VERSION;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetNewVersionResponse.class;
    }
}
