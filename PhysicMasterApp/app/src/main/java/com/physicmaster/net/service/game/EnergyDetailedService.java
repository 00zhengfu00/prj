package com.physicmaster.net.service.game;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.game.EnergyDetailedResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class EnergyDetailedService extends OpenApiDataServiceBase {

    public EnergyDetailedService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.ENERGY;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return EnergyDetailedResponse.class;
    }
}
