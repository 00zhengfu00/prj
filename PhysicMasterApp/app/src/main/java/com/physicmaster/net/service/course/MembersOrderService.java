package com.physicmaster.net.service.course;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.course.MemberOrderResponse;

/**
 * Created by Administrator on 2016/11/21.
 */
public class MembersOrderService extends OpenApiDataServiceBase {
    public MembersOrderService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.MEMBER_MAKE;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return MemberOrderResponse.class;
    }
}
