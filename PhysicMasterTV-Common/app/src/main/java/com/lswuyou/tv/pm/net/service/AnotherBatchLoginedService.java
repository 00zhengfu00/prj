package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.course.AnotherBatchResponse;

/**
 * Created by huashigen on 2018-02-03.
 */

public class AnotherBatchLoginedService extends OpenApiDataServiceBase {

    public AnotherBatchLoginedService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
      return   ServiceURL.ANOTHER_BATCH_LOGINED;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return AnotherBatchResponse.class;
    }
}
