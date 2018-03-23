package com.physicmaster.net.service.game;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.game.EnergyResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class EnergyService extends OpenApiDataServiceBase {

    public EnergyService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.REFUSE_ENERGY_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return EnergyResponse.class;
    }
}
