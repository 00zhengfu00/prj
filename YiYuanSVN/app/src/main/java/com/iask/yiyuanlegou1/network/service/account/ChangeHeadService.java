package com.iask.yiyuanlegou1.network.service.account;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.CommonResponse;
import com.iask.yiyuanlegou1.network.respose.account.LoginResponse;
import com.iask.yiyuanlegou1.network.respose.account.LoginUserInfo;
import com.iask.yiyuanlegou1.network.respose.account.UpdateUserInfoData;

/**
 * Created by Administrator on 2016/6/3.
 */
public class ChangeHeadService extends OpenApiDataServiceBase {
    public ChangeHeadService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.CHANGE_HEAD_IMAGE;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return LoginResponse.class;
    }
}
