package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.ChagePwdResponse;

/**
 * Created by songrui on 16/11/16.
 */

public class ChagePwdService extends OpenApiDataServiceBase {

    public ChagePwdService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.RECOVER_PASSWD_OPWD;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return ChagePwdResponse.class;
    }
}
