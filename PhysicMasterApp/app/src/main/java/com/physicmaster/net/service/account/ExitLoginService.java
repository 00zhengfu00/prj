package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.ExitLoginResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class ExitLoginService extends OpenApiDataServiceBase {

    public ExitLoginService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.LOGOUT;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return ExitLoginResponse.class;
    }
}
