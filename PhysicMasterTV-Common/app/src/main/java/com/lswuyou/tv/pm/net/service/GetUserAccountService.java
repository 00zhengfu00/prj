package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.account.GetUserAccountResponse;

/**
 * Created by Administrator on 2016/8/17.
 */
public class GetUserAccountService extends OpenApiDataServiceBase {
    public GetUserAccountService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_USER_ACCOUNT;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetUserAccountResponse.class;
    }
}
