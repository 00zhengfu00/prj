package com.tt.lvruheng.eyepetizer.network.service;

import android.content.Context;

import com.tt.lvruheng.eyepetizer.network.OpenApiDataServiceBase;
import com.tt.lvruheng.eyepetizer.network.ServiceURL;
import com.tt.lvruheng.eyepetizer.network.response.ChangePwdResponse;

/**
 * Created by huashigen on 2018-01-31.
 */

public class ChangePwdService extends OpenApiDataServiceBase {

    public ChangePwdService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.CHANGE_PASSWD;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return ChangePwdResponse.class;
    }
}
