package com.iask.yiyuanlegou1.network.service.account;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.account.RefreshUserInfoResponse;

/**
 * Created by Administrator on 2016/6/13.
 */
public class RefreshUserInfoService extends OpenApiDataServiceBase {
    public RefreshUserInfoService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.REFRESH_USER_INFO;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return RefreshUserInfoResponse.class;
    }
}
