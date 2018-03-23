package com.physicmaster.net.service.im;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.im.GetInvitationListResponse;

/**
 * Created by huashigen on 2016/11/24.
 */
public class GetInvitationListService extends OpenApiDataServiceBase {
    public GetInvitationListService(Context pContext) {
        super(pContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.GET_INVITATION_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return GetInvitationListResponse.class;
    }
}
