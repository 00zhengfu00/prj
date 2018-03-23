package com.iask.yiyuanlegou1.network.service.product;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.product.ProductClassifyResponse;

/**
 * Created by Administrator on 2016/5/24.
 */
public class ProductClassifyService extends OpenApiDataServiceBase{
    public ProductClassifyService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.PRODUCT_CLASSIFY;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return ProductClassifyResponse.class;
    }
}
