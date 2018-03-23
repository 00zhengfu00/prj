package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.AuthCodeResponse;

/**
 * Created by huashigen on 2017/2/22.
 */

public class GetAuthCodeService extends OpenApiDataServiceBase {
    public GetAuthCodeService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_AUTH_CODE;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return AuthCodeResponse.class;
    }
}
