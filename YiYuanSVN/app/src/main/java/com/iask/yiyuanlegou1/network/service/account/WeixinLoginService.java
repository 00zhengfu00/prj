package com.iask.yiyuanlegou1.network.service.account;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.account.WeixinLoginResponse;

/**
 * Created by Administrator on 2016/5/30.
 */
public class WeixinLoginService extends OpenApiDataServiceBase {
    public WeixinLoginService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.WEIXIN_LOGIN;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return WeixinLoginResponse.class;
    }
}
