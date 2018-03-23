package com.physicmaster.net.service.course;

import android.content.Context;
import android.graphics.Path;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.course.GetCourseDetailReponse;

/**
 * Created by huashigen on 2016/11/21.
 */
public class CourseDetailService  extends OpenApiDataServiceBase{
    public CourseDetailService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_COURSE_DETAIL;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetCourseDetailReponse.class;
    }
}
