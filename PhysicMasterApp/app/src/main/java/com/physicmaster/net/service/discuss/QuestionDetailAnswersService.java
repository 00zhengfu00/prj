package com.physicmaster.net.service.discuss;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.discuss.QuestionDetailAnswerResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class QuestionDetailAnswersService extends OpenApiDataServiceBase {

    public QuestionDetailAnswersService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.ANSWER_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return QuestionDetailAnswerResponse.class;
    }
}
