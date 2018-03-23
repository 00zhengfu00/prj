package com.physicmaster.net.service.game;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.game.GoldMallResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class GoldMallService extends OpenApiDataServiceBase {

    public GoldMallService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.MALL;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GoldMallResponse.class;
    }
}
