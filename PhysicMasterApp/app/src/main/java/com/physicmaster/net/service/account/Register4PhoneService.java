package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.user.UserDataResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class Register4PhoneService extends OpenApiDataServiceBase {

    public Register4PhoneService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.REGISTER;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return UserDataResponse.class;
    }
}
