package com.physicmaster.net.service.discuss;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.discuss.QuestionListResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class QuestionListService extends OpenApiDataServiceBase {

    public QuestionListService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.QUESTION_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return QuestionListResponse.class;
    }
}
