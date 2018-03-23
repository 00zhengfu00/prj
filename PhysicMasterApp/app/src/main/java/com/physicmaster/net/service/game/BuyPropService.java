package com.physicmaster.net.service.game;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.game.BuyPropResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class BuyPropService extends OpenApiDataServiceBase {

    public BuyPropService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.BUY_PROP;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return BuyPropResponse.class;
    }
}
