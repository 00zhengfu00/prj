package com.physicmaster.net.service.course;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.course.MemberDetailsResponse;

/**
 * Created by Administrator on 2016/11/21.
 */
public class MembersDetailsService extends OpenApiDataServiceBase {
    public MembersDetailsService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.MEMBER_DETAILS;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return MemberDetailsResponse.class;
    }
}
