package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.course.GetPackCourseResponse;

/**
 * Created by Administrator on 2016/8/17.
 */
public class GetPackCourseLoginedService extends OpenApiDataServiceBase {
    public GetPackCourseLoginedService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_PACK_COURSE_DETAIL_LOGINED;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetPackCourseResponse.class;
    }
}
