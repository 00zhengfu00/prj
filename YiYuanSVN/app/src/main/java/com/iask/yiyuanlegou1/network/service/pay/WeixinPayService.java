package com.iask.yiyuanlegou1.network.service.pay;

import android.app.Service;
import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.pay.WeixinPayResponse;

/**
 * Created by Administrator on 2016/6/1.
 */
public class WeixinPayService extends OpenApiDataServiceBase {
    public WeixinPayService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.WEIXIN_PAY;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return WeixinPayResponse.class;
    }
}
