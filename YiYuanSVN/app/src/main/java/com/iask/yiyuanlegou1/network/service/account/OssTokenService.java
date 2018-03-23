package com.iask.yiyuanlegou1.network.service.account;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.account.OssTokenResponse;

/**
 * Created by Administrator on 2016/6/2.
 */
public class OssTokenService extends OpenApiDataServiceBase {
    public OssTokenService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.OSSTOKEN_GET;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return OssTokenResponse.class;
    }
}
