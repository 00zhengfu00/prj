package com.physicmaster.net.service.discuss;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.discuss.MessageResponse;


/**
 * Created by songrui on 16/11/16.
 */

public class MessageService extends OpenApiDataServiceBase {

    public MessageService(Context mContext) {
        super(mContext);
    }

    @Override
    protected String getResouceURI() {
        return ServiceURL.MESSAGE_LIST;
    }

    @Override
    protected Object getDataBeanTypeOrClass() {
        return MessageResponse.class;
    }
}
