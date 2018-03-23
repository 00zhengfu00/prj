package com.tt.lvruheng.eyepetizer.network.service;

import android.content.Context;

import com.tt.lvruheng.eyepetizer.network.OpenApiDataServiceBase;
import com.tt.lvruheng.eyepetizer.network.Response;
import com.tt.lvruheng.eyepetizer.network.ServiceURL;

/**
 * Created by huashigen on 2018-02-01.
 */

public class LogoutService extends OpenApiDataServiceBase {

    public LogoutService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.LOGOUT;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return Response.class;
    }
}
