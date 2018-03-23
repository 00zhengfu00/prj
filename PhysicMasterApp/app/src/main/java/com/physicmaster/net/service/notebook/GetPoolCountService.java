package com.physicmaster.net.service.notebook;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.notebook.GetPoolCountResponse;

/**
 * Created by huashigen on 2018-01-22.
 */

public class GetPoolCountService extends OpenApiDataServiceBase {

    public GetPoolCountService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_POOL_COUNT;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetPoolCountResponse.class;
    }
}
