package com.physicmaster.net.service.notebook;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.notebook.GetPoolListRespose;

/**
 * Created by huashigen on 2018-01-06.
 */

public class GetPoolQuListService extends OpenApiDataServiceBase {

    public GetPoolQuListService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GETPOOLQULIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetPoolListRespose.class;
    }
}
