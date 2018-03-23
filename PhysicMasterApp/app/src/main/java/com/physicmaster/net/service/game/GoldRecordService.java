package com.physicmaster.net.service.game;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.game.GoldRecrodResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class GoldRecordService extends OpenApiDataServiceBase {

    public GoldRecordService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.TRANSACTION;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GoldRecrodResponse.class;
    }
}
