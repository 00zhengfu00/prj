package com.physicmaster.net.service.excercise;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.excercise.GetChapterDetailsResponse;
import com.physicmaster.net.response.excercise.GetChapterListResponse;

/**
 * Created by huashigen on 2017-07-05.
 */

public class GetChapterListService extends OpenApiDataServiceBase {
    public GetChapterListService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.CHAPTER_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetChapterListResponse.class;
    }
}
