package com.physicmaster.net.service.discuss;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.discuss.AnswerListResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class AnswerListService extends OpenApiDataServiceBase {

    public AnswerListService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.ANSWER_MYLIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return AnswerListResponse.class;
    }
}
