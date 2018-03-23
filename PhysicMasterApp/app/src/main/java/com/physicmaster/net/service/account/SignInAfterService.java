package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.SignInResponse;

/**
 * Created by huashigen on 2017/1/18.
 */

public class SignInAfterService extends OpenApiDataServiceBase {
    public SignInAfterService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.SIGN_IN_AFTER;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return SignInResponse.class;
    }
}
