package com.physicmaster.net.service.course;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.CommonResponse;

/**
 * Created by huashigen on 2016/11/30.
 */
public class CollectVideoService extends OpenApiDataServiceBase {
    public CollectVideoService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.COLLECT_VIDEO;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return CommonResponse.class;
    }
}
