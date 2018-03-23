package com.tt.lvruheng.eyepetizer.network.service;

import android.content.Context;

import com.tt.lvruheng.eyepetizer.network.OpenApiDataServiceBase;
import com.tt.lvruheng.eyepetizer.network.ServiceURL;
import com.tt.lvruheng.eyepetizer.network.response.IdentityResponse;

/**
 * Created by huashigen on 2018-03-01.
 */

public class GetIdentityService extends OpenApiDataServiceBase {

    public GetIdentityService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_IDENTITY;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return IdentityResponse.class;
    }
}
