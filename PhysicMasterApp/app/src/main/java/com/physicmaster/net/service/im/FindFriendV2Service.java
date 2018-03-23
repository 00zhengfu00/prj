package com.physicmaster.net.service.im;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.im.FindFriendResponse;
import com.physicmaster.net.response.im.FindFriendV2Response;

/**
 * Created by huashigen on 2016/11/24.
 */
public class FindFriendV2Service extends OpenApiDataServiceBase {
    public FindFriendV2Service(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.FIND_FRIEND_V2;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return FindFriendV2Response.class;
    }
}
