package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.course.GetRecommandCoursesResponse;

/**
 * Created by Administrator on 2016/8/25.
 */
public class GetRecommandService extends OpenApiDataServiceBase {
    public GetRecommandService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_RECOMMAND_COURSES;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetRecommandCoursesResponse.class;
    }
}
