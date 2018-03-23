package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.order.GetOrderStatusResponse;

/**
 * Created by Administrator on 2016/8/18.
 */
public class GetOrderStatusService extends OpenApiDataServiceBase {
    public GetOrderStatusService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_ORDER_STATUS;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetOrderStatusResponse.class;
    }
}
