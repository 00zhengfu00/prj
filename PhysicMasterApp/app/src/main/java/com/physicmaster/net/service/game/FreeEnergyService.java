package com.physicmaster.net.service.game;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.game.FreeEnergyResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class FreeEnergyService extends OpenApiDataServiceBase {

    public FreeEnergyService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_INVITE_ENERGY;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return FreeEnergyResponse.class;
    }
}
