package com.tt.lvruheng.eyepetizer.network.service;

import android.content.Context;

import com.tt.lvruheng.eyepetizer.network.OpenApiDataServiceBase;
import com.tt.lvruheng.eyepetizer.network.ServiceURL;
import com.tt.lvruheng.eyepetizer.network.response.IdCardOcrResponse;

/**
 * Created by huashigen on 2018-01-31.
 */

public class IdCardOcrService extends OpenApiDataServiceBase {

    public IdCardOcrService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.IDCARD_OCR;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return IdCardOcrResponse.class;
    }
}
