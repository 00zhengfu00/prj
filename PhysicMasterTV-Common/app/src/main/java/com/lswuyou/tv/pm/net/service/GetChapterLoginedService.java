package com.lswuyou.tv.pm.net.service;

import android.content.Context;

import com.lswuyou.tv.pm.net.OpenApiDataServiceBase;
import com.lswuyou.tv.pm.net.ServiceURL;
import com.lswuyou.tv.pm.net.response.course.GetChapterResponse;

/**
 * Created by Administrator on 2016/8/17.
 */
public class GetChapterLoginedService extends OpenApiDataServiceBase {
    public GetChapterLoginedService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_CHAPTER_DETAIL_LOGINED;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetChapterResponse.class;
    }
}
