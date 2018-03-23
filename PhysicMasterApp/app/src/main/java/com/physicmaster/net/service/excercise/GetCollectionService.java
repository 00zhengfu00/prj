package com.physicmaster.net.service.excercise;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.excercise.GetCollectionResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class GetCollectionService extends OpenApiDataServiceBase {

    public GetCollectionService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.VIDEO_COLLECTION;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetCollectionResponse.class;
    }
}
