package com.iask.yiyuanlegou1.network.service.product;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.product.MyShareResponse;

/**
 * Created by Administrator on 2016/6/6.
 */
public class MyShareService extends OpenApiDataServiceBase {
    public MyShareService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.MY_SHARE;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return MyShareResponse.class;
    }
}
