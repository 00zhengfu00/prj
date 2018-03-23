package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.user.UserDataResponse;

/**
 * Created by huashigen on 2016/12/16.
 */

public class BindService extends OpenApiDataServiceBase {
    public BindService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.BIND_OPEN;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return UserDataResponse.class;
    }
}
