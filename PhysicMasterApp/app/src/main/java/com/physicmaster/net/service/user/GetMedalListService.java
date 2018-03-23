package com.physicmaster.net.service.user;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.user.GetMedalListResponse;

/**
 * Created by huashigen on 2016/11/21.
 */
public class GetMedalListService extends OpenApiDataServiceBase{
    public GetMedalListService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.MEDAL_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetMedalListResponse.class;
    }
}
