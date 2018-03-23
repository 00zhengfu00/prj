package com.physicmaster.net.service.user;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.user.GetCourseResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class GetCourseService extends OpenApiDataServiceBase {

    public GetCourseService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.COURSE_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetCourseResponse.class;
    }
}
