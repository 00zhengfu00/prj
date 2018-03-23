package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.account.RegisterByQdResponse;

/**
 * Created by Administrator on 2016/10/24.
 */
public class RegisterByQdService extends OpenApiDataServiceBase {
    public RegisterByQdService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.REGISTER_BY_CHANNEL;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return RegisterByQdResponse.class;
    }
}
