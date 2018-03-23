package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.FindPwdResponse;

/**
 * Created by songrui on 16/11/16.
 */

public class FindPwd4PhoneService extends OpenApiDataServiceBase {

    public FindPwd4PhoneService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.RECOVER_PASSWD_PHONE;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return FindPwdResponse.class;
    }
}
