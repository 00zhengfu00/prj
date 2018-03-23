package com.physicmaster.net.service.excercise;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.excercise.GetMemoResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class GetMemoService extends OpenApiDataServiceBase {

    public GetMemoService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.MEMO_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetMemoResponse.class;
    }
}
