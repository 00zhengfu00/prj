package com.iask.yiyuanlegou1.network.service.product;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.product.LatestPublishResponse;

/**
 * Created by Administrator on 2016/5/21.
 */
public class LastedPublishProductService extends OpenApiDataServiceBase{
    public LastedPublishProductService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.LATEST_PUBLISH;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return LatestPublishResponse.class;
    }
}
