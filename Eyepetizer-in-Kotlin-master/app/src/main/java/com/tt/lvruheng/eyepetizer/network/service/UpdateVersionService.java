package com.tt.lvruheng.eyepetizer.network.service;

import android.content.Context;

import com.tt.lvruheng.eyepetizer.network.OpenApiDataServiceBase;
import com.tt.lvruheng.eyepetizer.network.ServiceURL;
import com.tt.lvruheng.eyepetizer.network.response.UpdateVersionResponse;

/**
 * Created by huashigen on 2018-01-31.
 */

public class UpdateVersionService extends OpenApiDataServiceBase {

    public UpdateVersionService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.VERSION_UPDATE;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return UpdateVersionResponse.class;
    }
}
