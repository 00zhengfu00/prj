package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.ForgetPwdResponse;

/**
 * Created by songrui on 16/11/16.
 */

public class ForgetPwdService extends OpenApiDataServiceBase {

    public ForgetPwdService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.VERIFT_PASSWD;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return ForgetPwdResponse.class;
    }
}
