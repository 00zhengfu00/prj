package com.physicmaster.net.service.discuss;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.discuss.AnswerDetailResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class AnswerDetailService extends OpenApiDataServiceBase {

    public AnswerDetailService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.ANSWER_DELAIL;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return AnswerDetailResponse.class;
    }
}
