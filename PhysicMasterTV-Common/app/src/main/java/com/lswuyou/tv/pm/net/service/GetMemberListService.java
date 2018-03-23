package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.GetMemberListResponse;

/**
 * Created by huashigen on 2017-09-07.
 */

public class GetMemberListService extends OpenApiDataServiceBase{
    public GetMemberListService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_MEMBER_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetMemberListResponse.class;
    }
}
