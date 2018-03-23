package com.physicmaster.net.service.excercise;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.excercise.GetTopicmapsResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class GetTopicmapsService extends OpenApiDataServiceBase {

    public GetTopicmapsService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.QUESTION_WRONG;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetTopicmapsResponse.class;
    }
}
