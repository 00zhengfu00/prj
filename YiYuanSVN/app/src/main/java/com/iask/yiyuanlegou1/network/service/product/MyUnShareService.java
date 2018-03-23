package com.iask.yiyuanlegou1.network.service.product;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.product.MyUnShareResponse;

/**
 * Created by Administrator on 2016/6/20.
 */
public class MyUnShareService extends OpenApiDataServiceBase{
    public MyUnShareService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.MY_UN_SHARE;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return MyUnShareResponse.class;
    }
}
