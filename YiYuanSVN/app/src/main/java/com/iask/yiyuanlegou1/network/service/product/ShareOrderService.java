package com.iask.yiyuanlegou1.network.service.product;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.product.ShareOrderResponse;

/**
 * Created by Administrator on 2016/5/25.
 */
public class ShareOrderService extends OpenApiDataServiceBase {
    public ShareOrderService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.SHARE_ORDER;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return ShareOrderResponse.class;
    }
}
