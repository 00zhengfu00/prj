package com.physicmaster.net.service.discuss;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.discuss.QuestionDetailResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class QuestionDetailService extends OpenApiDataServiceBase {

    public QuestionDetailService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.QUESTION_DETAIL;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return QuestionDetailResponse.class;
    }
}
