package com.physicmaster.net.service.course;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.course.MemberPayResponse;

/**
 * Created by huashigen on 2016/11/22.
 */
public class MemberPayService extends OpenApiDataServiceBase {
    public MemberPayService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.MEMBER_PREPAY;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return MemberPayResponse.class;
    }
}
