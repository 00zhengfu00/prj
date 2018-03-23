package com.physicmaster.net.service.game;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.game.AgreeEnergyResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class AgreeEnergyService extends OpenApiDataServiceBase {

    public AgreeEnergyService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.RECEIVE_ENERGY;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return AgreeEnergyResponse.class;
    }
}
