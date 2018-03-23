package com.physicmaster.net.service.game;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.game.UsePropResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class UsePropService extends OpenApiDataServiceBase {

    public UsePropService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.USE_PRPO;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return UsePropResponse.class;
    }
}
