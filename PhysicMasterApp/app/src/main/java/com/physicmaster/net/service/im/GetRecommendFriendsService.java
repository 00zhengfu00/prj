package com.physicmaster.net.service.im;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.im.GetRecommendFriendsResponse;

/**
 * Created by huashigen on 2017/6/1.
 */

public class GetRecommendFriendsService extends OpenApiDataServiceBase {
    public GetRecommendFriendsService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.RECOMMEND_FRIENDS;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetRecommendFriendsResponse.class;
    }
}
