package com.physicmaster.net.service.explore;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.explore.GetExploreResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class GetExploreService extends OpenApiDataServiceBase {

    public GetExploreService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.EXPLORE_INDEX;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetExploreResponse.class;
    }
}
