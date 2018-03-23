package com.physicmaster.net.service.excercise;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.excercise.GetTopicmapAnalysisResponse;


public class GetTopicmapAnalysisService extends OpenApiDataServiceBase {

	public GetTopicmapAnalysisService(Context pContext) {
		super(pContext);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String getResouceURI() {
		// TODO Auto-generated method stub
		return ServiceURL.QUESTION_WRONG_LIST;
	}

	@Override
	protected Object getDataBeanTypeOrClass() {
		// TODO Auto-generated method stub
		return GetTopicmapAnalysisResponse.class;
	}

}
