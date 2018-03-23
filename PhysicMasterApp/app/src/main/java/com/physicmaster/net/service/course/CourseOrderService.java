package com.physicmaster.net.service.course;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.course.CourseOrderResponse;

/**
 * Created by Administrator on 2016/11/21.
 */
public class CourseOrderService extends OpenApiDataServiceBase {
    public CourseOrderService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.COURSE_ORDER;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return CourseOrderResponse.class;
    }
}
