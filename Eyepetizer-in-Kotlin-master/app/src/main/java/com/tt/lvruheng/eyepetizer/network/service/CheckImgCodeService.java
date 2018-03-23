package com.tt.lvruheng.eyepetizer.network.service;

import android.content.Context;

import com.tt.lvruheng.eyepetizer.network.OpenApiDataServiceBase;
import com.tt.lvruheng.eyepetizer.network.ServiceURL;
import com.tt.lvruheng.eyepetizer.network.response.CheckImgCodeResponse;

/**
 * Created by huashigen on 2018-01-31.
 */

public class CheckImgCodeService extends OpenApiDataServiceBase {

    public CheckImgCodeService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.CHECK_IMG_CODE;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return CheckImgCodeResponse.class;
    }
}
