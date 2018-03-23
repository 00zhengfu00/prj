package com.physicmaster.net.service.course;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.course.CourseExcerciseResponse;

/**
 * Created by huashigen on 2016/11/26.
 */
public class CourseExcerciseService extends OpenApiDataServiceBase {
    public CourseExcerciseService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.COURSE_EXCERCISE;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return CourseExcerciseResponse.class;
    }
}
