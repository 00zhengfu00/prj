package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.account.VerifyTokenResponse;

/**
 * Created by huashigen on 2017-09-11.
 */

public class VerifyTokenService extends OpenApiDataServiceBase {

    public VerifyTokenService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.VERIFY_TOKEN;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return VerifyTokenResponse.class;
    }
}
