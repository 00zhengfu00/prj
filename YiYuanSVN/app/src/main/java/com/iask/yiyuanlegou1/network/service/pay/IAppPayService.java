package com.iask.yiyuanlegou1.network.service.pay;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.pay.IAppPayResponse;

/**
 * Created by Administrator on 2016/6/1.
 */
public class IAppPayService extends OpenApiDataServiceBase {
    public IAppPayService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.IAPP_PAY;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return IAppPayResponse.class;
    }
}
