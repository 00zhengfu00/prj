package com.physicmaster.net.service.game;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.game.DeleteFreshResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class DeleteFreshService extends OpenApiDataServiceBase {

    public DeleteFreshService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.IGNORE_FRESH_NEWS;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return DeleteFreshResponse.class;
    }
}
