package com.physicmaster.net.service.ranking;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.ranking.GetRankingResponse;

/**
 * Created by huashigen on 2016/11/26.
 */
public class GetRankingService extends OpenApiDataServiceBase {
    public GetRankingService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.RANK_DATA;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetRankingResponse.class;
    }
}
