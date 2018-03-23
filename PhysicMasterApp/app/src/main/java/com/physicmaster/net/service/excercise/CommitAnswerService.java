package com.physicmaster.net.service.excercise;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.excercise.CommitAnswerResponse;

/**
 * Created by songrui on 16/11/29.
 */

public class CommitAnswerService extends OpenApiDataServiceBase {
    public CommitAnswerService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.COMMIT_ANWSER;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return CommitAnswerResponse.class;
    }
}
