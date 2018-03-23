package com.physicmaster.net.service.excercise;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.excercise.GetVideoDetailsResponse;

/**
 * Created by huashigen on 2017-07-05.
 */

public class GetVideoDetailsService extends OpenApiDataServiceBase {

    public GetVideoDetailsService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.VIDEO_DETAILS;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetVideoDetailsResponse.class;
    }
}
