package com.physicmaster.net.service.discuss;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.CommonResponse;

/**
 * Created by huashigen on 2017/5/15.
 */

public class PublishQuestionService extends OpenApiDataServiceBase {
    public PublishQuestionService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.QUESTION_RELEASE;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return CommonResponse.class;
    }
}
