package com.iask.yiyuanlegou1.network.service.product;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.product.ProductDetailResponse;

/**
 * Created by Administrator on 2016/5/21.
 */
public class ProductDetailService extends OpenApiDataServiceBase {
    public ProductDetailService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.PRODUCT_DETAIL;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return ProductDetailResponse.class;
    }
}
