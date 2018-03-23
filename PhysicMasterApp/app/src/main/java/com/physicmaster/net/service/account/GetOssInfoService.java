package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.GetOssInfoResponse;

/**
 * Created by huashigen on 2018-01-25.
 */

public class GetOssInfoService extends OpenApiDataServiceBase {

    public GetOssInfoService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_OSS_INFO;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetOssInfoResponse.class;
    }
}
