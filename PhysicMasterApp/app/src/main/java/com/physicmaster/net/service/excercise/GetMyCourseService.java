package com.physicmaster.net.service.excercise;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.excercise.GetMyCourseResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class GetMyCourseService extends OpenApiDataServiceBase {

    public GetMyCourseService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.MY_COURSE;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetMyCourseResponse.class;
    }
}
