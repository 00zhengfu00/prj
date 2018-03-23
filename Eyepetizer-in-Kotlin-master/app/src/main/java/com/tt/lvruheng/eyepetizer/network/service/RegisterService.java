package com.tt.lvruheng.eyepetizer.network.service;

import android.content.Context;

import com.tt.lvruheng.eyepetizer.network.OpenApiDataServiceBase;
import com.tt.lvruheng.eyepetizer.network.ServiceURL;
import com.tt.lvruheng.eyepetizer.network.response.RegisterResponse;

/**
 * Created by huashigen on 2018-01-31.
 */

public class RegisterService extends OpenApiDataServiceBase {

    public RegisterService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.SIGNON;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return RegisterResponse.class;
    }
}
