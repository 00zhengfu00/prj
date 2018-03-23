package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.account.WeixinLoginResponse;

/**
 * Created by Administrator on 2016/8/16.
 */
public class WeixinLoginService extends OpenApiDataServiceBase {
    public WeixinLoginService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.WEIXIN_LOGIN;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return WeixinLoginResponse.class;
    }
}
