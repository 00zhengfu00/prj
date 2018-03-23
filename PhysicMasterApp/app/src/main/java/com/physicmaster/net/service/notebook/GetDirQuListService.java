package com.physicmaster.net.service.notebook;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.notebook.GetPoolListRespose;

/**
 * Created by huashigen on 2018-01-06.
 */

public class GetDirQuListService extends OpenApiDataServiceBase {

    public GetDirQuListService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GETDIRQULIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetPoolListRespose.class;
    }
}
