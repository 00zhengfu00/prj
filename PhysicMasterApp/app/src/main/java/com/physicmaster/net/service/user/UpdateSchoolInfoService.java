/* 
 * 系统名称：lswuyou
 * 类  名  称：UpdateSchoolInfoService.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0.0
 * 开发人员： huashigen
 * 开发时间： 2015-9-18 下午5:54:45
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.physicmaster.net.service.user;

import android.content.Context;

import com.physicmaster.net.OpenApiDataServiceBase;
import com.physicmaster.net.ServiceURL;
import com.physicmaster.net.response.user.UserDataResponse;

public class UpdateSchoolInfoService extends OpenApiDataServiceBase {

	public UpdateSchoolInfoService(Context pContext) {
		super(pContext);
	}

	@Override
	protected String getResouceURI() {
		return ServiceURL.UPDATE_USER_SCHOOL;
	}

	@Override
	protected Object getDataBeanTypeOrClass() {
		return UserDataResponse.class;
	}

}
