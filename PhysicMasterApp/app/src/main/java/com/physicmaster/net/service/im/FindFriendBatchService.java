package com.physicmaster.net.service.im;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.im.FindFriendBatchResponse;

/**
 * Created by huashigen on 2017/5/16.
 */

public class FindFriendBatchService extends OpenApiDataServiceBase {
    public FindFriendBatchService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.FIND_FRIEND_BATCH;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return FindFriendBatchResponse.class;
    }
}
