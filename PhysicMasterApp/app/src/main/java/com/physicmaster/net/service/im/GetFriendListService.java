package com.physicmaster.net.service.im;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.im.GetFriendListResponse;

/**
 * Created by huashigen on 2016/11/24.
 */
public class GetFriendListService extends OpenApiDataServiceBase {
    public GetFriendListService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_FRIEND_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetFriendListResponse.class;
    }
}
