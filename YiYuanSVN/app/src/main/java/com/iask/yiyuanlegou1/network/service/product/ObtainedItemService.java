package com.iask.yiyuanlegou1.network.service.product;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.product.ObtainedItemResponse;

/**
 * Created by Administrator on 2016/6/13.
 */
public class ObtainedItemService extends OpenApiDataServiceBase {
    public ObtainedItemService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.OBTAINED_ITEM;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return ObtainedItemResponse.class;
    }
}
