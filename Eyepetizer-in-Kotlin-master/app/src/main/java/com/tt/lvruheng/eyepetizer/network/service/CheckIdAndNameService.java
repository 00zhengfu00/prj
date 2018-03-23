package com.tt.lvruheng.eyepetizer.network.service;

import android.content.Context;

import com.tt.lvruheng.eyepetizer.network.OpenApiDataServiceBase;
import com.tt.lvruheng.eyepetizer.network.ServiceURL;

/**
 * Created by huashigen on 2018-01-31.
 */

public class CheckIdAndNameService extends OpenApiDataServiceBase {

    public CheckIdAndNameService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.CHECK_ID_AND_NAME;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return null;
    }
}
