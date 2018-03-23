package com.physicmaster.net.service.game;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.game.RankResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class RankService extends OpenApiDataServiceBase {

    public RankService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.RANK;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return RankResponse.class;
    }
}
