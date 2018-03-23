package com.physicmaster.net.service.course;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.course.CheckOrderResponse;

/**
 * Created by huashigen on 2016/11/23.
 */
public class CheckOrderService extends OpenApiDataServiceBase {
    public CheckOrderService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.CHECK_ORDER;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return CheckOrderResponse.class;
    }
}
