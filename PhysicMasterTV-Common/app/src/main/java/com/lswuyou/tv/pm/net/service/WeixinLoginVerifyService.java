package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.account.WeixinLoginVerifyResponse;

/**
 * Created by Administrator on 2016/9/18.
 */
public class WeixinLoginVerifyService extends OpenApiDataServiceBase {
    public WeixinLoginVerifyService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.WEXIN_LOGIN_VERIFY;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return WeixinLoginVerifyResponse.class;
    }
}
