package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.account.GetLoginCfgResponse;

/**
 * Created by Administrator on 2016/8/15.
 */
public class GetLoginCfgService extends OpenApiDataServiceBase {
    public GetLoginCfgService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_LOGIN_CFG_URL;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetLoginCfgResponse.class;
    }
}
