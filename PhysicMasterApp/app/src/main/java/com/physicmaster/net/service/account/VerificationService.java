package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.VerificationResponse;



public class VerificationService extends OpenApiDataServiceBase {

	public VerificationService(Context pContext) {
		super(pContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getResouceURI() {
		// TODO Auto-generated method stub
		return ServiceURL.GET_VERIFICATION;
	}

	@Override
	protected Object getDataBeanTypeOrClass() {
		// TODO Auto-generated method stub
		return VerificationResponse.class;
	}

}
