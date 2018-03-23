package com.physicmaster.net.service.user;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.user.UserDataResponse;


public class ChageUserService extends OpenApiDataServiceBase {

	public ChageUserService(Context pContext) {
		super(pContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getResouceURI() {
		// TODO Auto-generated method stub
		return ServiceURL.UPDATE_USERINFO;
	}

	@Override
	protected Object getDataBeanTypeOrClass() {
		// TODO Auto-generated method stub
		return UserDataResponse.class;
	}

}
