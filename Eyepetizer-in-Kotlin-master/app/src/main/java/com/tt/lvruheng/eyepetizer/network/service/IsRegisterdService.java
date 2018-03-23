package com.tt.lvruheng.eyepetizer.network.service;

import android.content.Context;

import com.tt.lvruheng.eyepetizer.network.OpenApiDataServiceBase;
import com.tt.lvruheng.eyepetizer.network.ServiceURL;
import com.tt.lvruheng.eyepetizer.network.response.IsRegisteredResponse;

/**
 * Created by huashigen on 2018-01-31.
 */

public class IsRegisterdService extends OpenApiDataServiceBase {

    public IsRegisterdService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.IS_REGISTERED;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return IsRegisteredResponse.class;
    }
}
