package com.physicmaster.net.service.game;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.game.GetTaskResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class GetTaskService extends OpenApiDataServiceBase {

    public GetTaskService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.TASK_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetTaskResponse.class;
    }
}
