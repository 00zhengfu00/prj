package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.LoginByOpenResponse;

/**
 * Created by huashigen on 2016/11/23.
 */
public class LoginByOpenService extends OpenApiDataServiceBase {
    public LoginByOpenService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.LOGIN_BY_OPEN;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return LoginByOpenResponse.class;
    }
}
