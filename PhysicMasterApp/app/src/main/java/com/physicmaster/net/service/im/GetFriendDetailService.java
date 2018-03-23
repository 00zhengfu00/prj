package com.physicmaster.net.service.im;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.im.GetFriendDetailResponse;

/**
 * Created by huashigen on 2016/11/24.
 */
public class GetFriendDetailService extends OpenApiDataServiceBase {
    public GetFriendDetailService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_FRIEND_DETAIL;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetFriendDetailResponse.class;
    }
}
