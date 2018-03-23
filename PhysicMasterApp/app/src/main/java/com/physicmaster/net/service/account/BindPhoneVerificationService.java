package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.BindPhoneVerificationResponse;


public class BindPhoneVerificationService extends OpenApiDataServiceBase {

	public BindPhoneVerificationService(Context pContext) {
		super(pContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getResouceURI() {
		// TODO Auto-generated method stub
		return ServiceURL.BIND_PHONE_VERIFY;
	}

	@Override
	protected Object getDataBeanTypeOrClass() {
		// TODO Auto-generated method stub
		return BindPhoneVerificationResponse.class;
	}

}
