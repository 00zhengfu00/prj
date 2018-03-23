package com.physicmaster.net.service.study;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.study.StudyingInfoResponse;

/**
 * Created by huashigen on 2017-10-16.
 */

public class GetStudyingInfoService extends OpenApiDataServiceBase {
    public GetStudyingInfoService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.STUDYING_INFO;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return StudyingInfoResponse.class;
    }
}
