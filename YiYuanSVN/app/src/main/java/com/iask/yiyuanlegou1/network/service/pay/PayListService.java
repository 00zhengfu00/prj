package com.iask.yiyuanlegou1.network.service.pay;

import android.content.Context;

import com.iask.yiyuanlegou1.network.OpenApiDataServiceBase;
import com.iask.yiyuanlegou1.network.ServiceURL;
import com.iask.yiyuanlegou1.network.respose.pay.PayListResponse;

/**
 * Created by Administrator on 2016/5/31.
 */
public class PayListService extends OpenApiDataServiceBase{
    public PayListService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.PAYWAY_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return PayListResponse.class;
    }
}
