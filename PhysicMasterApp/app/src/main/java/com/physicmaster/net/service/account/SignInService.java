package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.SignInResponse;

/**
 * Created by huashigen on 2017/1/18.
 */

public class SignInService extends OpenApiDataServiceBase {
    public SignInService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.SIGN_IN;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return SignInResponse.class;
    }
}
