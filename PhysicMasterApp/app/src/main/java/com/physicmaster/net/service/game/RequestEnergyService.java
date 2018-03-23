package com.physicmaster.net.service.game;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.game.RequestEnergyResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class RequestEnergyService extends OpenApiDataServiceBase {

    public RequestEnergyService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.REQUEST_ENERGY;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return RequestEnergyResponse.class;
    }
}
