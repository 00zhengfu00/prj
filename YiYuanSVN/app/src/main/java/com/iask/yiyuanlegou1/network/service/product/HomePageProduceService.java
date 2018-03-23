package com.iask.yiyuanlegou1.network.service.product;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.home.HomePageProductResponse;

/**
 * Created by Administrator on 2016/5/21.
 */
public class HomePageProduceService extends OpenApiDataServiceBase {


    public HomePageProduceService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.HOMEPAGE_DATA;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return HomePageProductResponse.class;
    }
}
