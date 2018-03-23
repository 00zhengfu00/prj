package com.physicmaster.net.service.excercise;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.excercise.AddMemoResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class AddMemoService extends OpenApiDataServiceBase {

    public AddMemoService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.ADD_MEMO;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return AddMemoResponse.class;
    }
}
