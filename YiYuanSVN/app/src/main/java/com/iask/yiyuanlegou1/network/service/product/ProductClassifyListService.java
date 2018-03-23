package com.iask.yiyuanlegou1.network.service.product;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.home.HomePageProductResponse;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ProductClassifyListService extends OpenApiDataServiceBase{
    public ProductClassifyListService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.PRODUCT_CLASSIFY_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return HomePageProductResponse.class;
    }
}
