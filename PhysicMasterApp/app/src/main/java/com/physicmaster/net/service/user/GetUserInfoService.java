package com.physicmaster.net.service.user;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.user.GetUserInfoResponse;

/**
 * Created by huashigen on 2016/11/21.
 */
public class GetUserInfoService  extends OpenApiDataServiceBase{
    public GetUserInfoService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_OTHER_USER_INFO;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetUserInfoResponse.class;
    }
}
