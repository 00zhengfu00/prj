package com.tt.lvruheng.eyepetizer.network.service;

import android.content.Context;

import com.tt.lvruheng.eyepetizer.network.OpenApiDataServiceBase;
import com.tt.lvruheng.eyepetizer.network.Response;
import com.tt.lvruheng.eyepetizer.network.ServiceURL;
import com.tt.lvruheng.eyepetizer.network.response.ChangePwdResponse;

/**
 * Created by huashigen on 2018-01-31.
 */

public class ResetPwdService extends OpenApiDataServiceBase {

    public ResetPwdService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.FIND_PASSWD;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return ChangePwdResponse.class;
    }
}
