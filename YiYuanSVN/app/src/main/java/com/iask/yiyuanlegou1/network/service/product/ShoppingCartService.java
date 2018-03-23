package com.iask.yiyuanlegou1.network.service.product;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.product.ShoppingCartResponse;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ShoppingCartService extends OpenApiDataServiceBase {
    public ShoppingCartService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.SHOPPINGCART;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return ShoppingCartResponse.class;
    }
}
