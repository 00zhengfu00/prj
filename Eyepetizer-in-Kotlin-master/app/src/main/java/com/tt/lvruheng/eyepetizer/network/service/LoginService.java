package com.tt.lvruheng.eyepetizer.network.service;

import android.content.Context;

import com.tt.lvruheng.eyepetizer.network.OpenApiDataServiceBase;
import com.tt.lvruheng.eyepetizer.network.ServiceURL;
import com.tt.lvruheng.eyepetizer.network.response.LoginResponse;

/**
 * Created by huashigen on 2018-01-31.
 */

public class LoginService extends OpenApiDataServiceBase {

    public LoginService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.LOGIN;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return LoginResponse.class;
    }
}
