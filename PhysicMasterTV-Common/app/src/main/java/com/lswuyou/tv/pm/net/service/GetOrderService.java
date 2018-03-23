package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.order.GetOrderResponse;

/**
 * Created by Administrator on 2016/8/18.
 */
public class GetOrderService extends OpenApiDataServiceBase {
    public GetOrderService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_COURSE_ORDER;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetOrderResponse.class;
    }
}
