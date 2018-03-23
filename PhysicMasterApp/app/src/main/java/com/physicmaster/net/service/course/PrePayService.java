package com.physicmaster.net.service.course;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.course.PrePayResponse;

/**
 * Created by huashigen on 2016/11/22.
 */
public class PrePayService extends OpenApiDataServiceBase {
    public PrePayService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.PREPAY;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return PrePayResponse.class;
    }
}
