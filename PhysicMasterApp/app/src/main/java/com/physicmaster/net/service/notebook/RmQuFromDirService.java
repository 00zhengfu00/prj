package com.physicmaster.net.service.notebook;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.CommonResponse;

/**
 * Created by huashigen on 2018-01-09.
 */

public class RmQuFromDirService extends OpenApiDataServiceBase {

    public RmQuFromDirService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.RM_QU_FROM_DIR;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return CommonResponse.class;
    }
}
