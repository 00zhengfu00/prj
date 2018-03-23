package com.physicmaster.net.service.notebook;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.notebook.GetTagResponse;

/**
 * Created by huashigen on 2017-12-22.
 */

public class GetTagService extends OpenApiDataServiceBase {

    public GetTagService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_DIR;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetTagResponse.class;
    }
}
