package com.physicmaster.net.service.member;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.course.MemberPayResponse;

/**
 * Created by huashigen on 2017-08-07.
 */

public class WebPrepayService extends OpenApiDataServiceBase {
    public WebPrepayService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.WEB_PREPAY;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return MemberPayResponse.class;
    }
}
