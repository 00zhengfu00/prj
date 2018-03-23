package com.physicmaster.net.service.im;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.im.FindFriendResponse;

/**
 * Created by huashigen on 2016/11/24.
 */
public class FindFriendService extends OpenApiDataServiceBase {
    public FindFriendService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.FIND_FRIEND;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return FindFriendResponse.class;
    }
}
