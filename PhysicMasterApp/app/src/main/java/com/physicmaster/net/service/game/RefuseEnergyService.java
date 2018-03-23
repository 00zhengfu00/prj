package com.physicmaster.net.service.game;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.game.RefuseEnergyResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class RefuseEnergyService extends OpenApiDataServiceBase {

    public RefuseEnergyService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.REFUSE_ENERGY;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return RefuseEnergyResponse.class;
    }
}
