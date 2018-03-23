package com.iask.yiyuanlegou1.network.service.product;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.product.NotObtainedItemResponse;

/**
 * Created by Administrator on 2016/6/13.
 */
public class NotObtainedItemService extends OpenApiDataServiceBase {
    public NotObtainedItemService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.NOT_OBTAINED_ITEM;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return NotObtainedItemResponse.class;
    }
}
