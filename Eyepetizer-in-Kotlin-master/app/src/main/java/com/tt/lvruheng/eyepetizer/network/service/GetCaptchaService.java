package com.tt.lvruheng.eyepetizer.network.service;

import android.content.Context;

import com.tt.lvruheng.eyepetizer.network.OpenApiDataServiceBase;
import com.tt.lvruheng.eyepetizer.network.ServiceURL;
import com.tt.lvruheng.eyepetizer.network.response.GetCaptchaResponse;

/**
 * Created by huashigen on 2018-01-31.
 */

public class GetCaptchaService extends OpenApiDataServiceBase {

    public GetCaptchaService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_CAPTCHA;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetCaptchaResponse.class;
    }
}
