package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.StartupResponse;

/**
 * Created by Administrator on 2016/11/18.
 */
public class StartupLoginedService extends OpenApiDataServiceBase{

    public StartupLoginedService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.STARTUP_LOGINED;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return StartupResponse.class;
    }
}
