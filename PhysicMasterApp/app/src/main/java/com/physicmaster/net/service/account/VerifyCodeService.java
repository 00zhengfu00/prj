package com.physicmaster.net.service.account;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.account.VerifyCodeResponse;


public class VerifyCodeService extends OpenApiDataServiceBase {

	public VerifyCodeService(Context pContext) {
		super(pContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getResouceURI() {
		// TODO Auto-generated method stub
		return ServiceURL.VERIFY_CODE;
	}

	@Override
	protected Object getDataBeanTypeOrClass() {
		// TODO Auto-generated method stub
		return VerifyCodeResponse.class;
	}

}
