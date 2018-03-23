package com.physicmaster.net.service.course;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.course.CourseIndexResponse;

/**
 * Created by huashigen on 2016/11/21.
 */
public class CourseIndexService extends OpenApiDataServiceBase {
    public CourseIndexService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.COURSE_INDEX;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return CourseIndexResponse.class;
    }
}
