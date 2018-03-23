package com.physicmaster.net.service.course;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.course.Course2ListResponse;
import com.physicmaster.net.response.excercise.GetChapterListResponse;

/**
 * Created by huashigen on 2017-07-12.
 */

public class GetChapterList2Service extends OpenApiDataServiceBase {
    public GetChapterList2Service(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.COURSE2LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetChapterListResponse.class;
    }
}
