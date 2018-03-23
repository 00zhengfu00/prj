package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.homepage.GetJingxuanListResponse;

/**
 * Created by huashigen on 2017-09-06.
 */

public class GetJingxuanListLoginedService extends OpenApiDataServiceBase {
    public GetJingxuanListLoginedService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_JINGXUAN_LIST_LOGINED;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetJingxuanListResponse.class;
    }
}
