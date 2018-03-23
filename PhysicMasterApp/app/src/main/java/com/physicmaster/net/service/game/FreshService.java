package com.physicmaster.net.service.game;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.game.FreshResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class FreshService extends OpenApiDataServiceBase {

    public FreshService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.FRESH_NEWS;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return FreshResponse.class;
    }
}
