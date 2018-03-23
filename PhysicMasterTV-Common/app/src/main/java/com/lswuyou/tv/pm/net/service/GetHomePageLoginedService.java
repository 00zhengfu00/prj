package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.homepage.GetHomePageDataResponse;

/**
 * Created by Administrator on 2016/8/15.
 */
public class GetHomePageLoginedService extends OpenApiDataServiceBase {
    public GetHomePageLoginedService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_HOME_PAGE_LOGINED;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetHomePageDataResponse.class;
    }
}
