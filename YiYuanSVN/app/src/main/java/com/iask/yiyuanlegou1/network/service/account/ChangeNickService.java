package com.iask.yiyuanlegou1.network.service.account;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.account.LoginResponse;

/**
 * Created by Administrator on 2016/6/12.
 */
public class ChangeNickService extends OpenApiDataServiceBase {
    public ChangeNickService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.CHANGE_NICK;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return LoginResponse.class;
    }
}
