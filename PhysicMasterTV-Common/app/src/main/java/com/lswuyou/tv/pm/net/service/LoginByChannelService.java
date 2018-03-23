package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.account.LoginByChannelResponse;

/**
 * Created by Administrator on 2016/10/14.
 */
public class LoginByChannelService extends OpenApiDataServiceBase {
    public LoginByChannelService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.LOGIN_BY_CHANNEL;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return LoginByChannelResponse.class;
    }
}
