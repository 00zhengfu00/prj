package com.physicmaster.net.service.excercise;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.excercise.GetChapterDetailsResponse;

/**
 * Created by huashigen on 2017-07-05.
 */

public class GetChapterDetailsService extends OpenApiDataServiceBase {
    public GetChapterDetailsService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.CHAPTER_DETAILS;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetChapterDetailsResponse.class;
    }
}
