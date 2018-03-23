package com.physicmaster.net.service.excercise;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.excercise.DeleteMemoResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class DeleteMemoService extends OpenApiDataServiceBase {

    public DeleteMemoService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.DELETE_MEMO;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return DeleteMemoResponse.class;
    }
}
