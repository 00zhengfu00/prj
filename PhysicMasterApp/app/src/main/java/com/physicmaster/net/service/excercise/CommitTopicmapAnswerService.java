package com.physicmaster.net.service.excercise;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.excercise.CommitTopicmapAnswerResponse;

/**
 * Created by songrui on 16/11/29.
 */

public class CommitTopicmapAnswerService extends OpenApiDataServiceBase {
    public CommitTopicmapAnswerService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.SUBMIT_QUESTION_WRONG;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return CommitTopicmapAnswerResponse.class;
    }
}
