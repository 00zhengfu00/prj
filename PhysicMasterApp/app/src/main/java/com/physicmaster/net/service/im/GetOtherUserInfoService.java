package com.physicmaster.net.service.im;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.im.GetOtherUserInfoResponse;

/**
 * Created by huashigen on 2017/5/18.
 */

public class GetOtherUserInfoService extends OpenApiDataServiceBase {
    public GetOtherUserInfoService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_OTHER_USER_INFO;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetOtherUserInfoResponse.class;
    }
}
