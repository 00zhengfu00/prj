package com.physicmaster.net.service.user;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.user.GetStudyInfoResponse;

/**
 * Created by huashigen on 2016/11/21.
 */
public class GetStudyInfoService extends OpenApiDataServiceBase{
    public GetStudyInfoService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.STUDY_INFO;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetStudyInfoResponse.class;
    }
}
