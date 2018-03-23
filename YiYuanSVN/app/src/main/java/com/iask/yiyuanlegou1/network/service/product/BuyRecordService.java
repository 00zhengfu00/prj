package com.iask.yiyuanlegou1.network.service.product;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.product.BuyRecordResponse;

/**
 * Created by Administrator on 2016/5/25.
 */
public class BuyRecordService extends OpenApiDataServiceBase{
    public BuyRecordService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.BUY_RECORD;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return BuyRecordResponse.class;
    }
}
