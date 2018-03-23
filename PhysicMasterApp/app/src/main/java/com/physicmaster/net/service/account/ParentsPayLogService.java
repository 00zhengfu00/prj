package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.FindPwdResponse;

/**
 * Created by huashigen on 2016/12/6.
 */
public class ParentsPayLogService extends OpenApiDataServiceBase {
    public ParentsPayLogService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.HELP_PAY_LOG;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return FindPwdResponse.class;
    }
}
