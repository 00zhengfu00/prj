package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.course.GetPackCourseResponse;

/**
 * Created by Administrator on 2016/8/17.
 */
public class GetPackCourseUnLoginService extends OpenApiDataServiceBase {
    public GetPackCourseUnLoginService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_PACK_COURSE_DETAIL_UNLOGIN;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetPackCourseResponse.class;
    }
}
