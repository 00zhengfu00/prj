/*
 * 系统名称：lswuyou
 * 类  名  称：IOpenApiDataServiceCallback.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0
 * 开发人员： huashigen
 * 开发时间： 2015-8-7 下午4:46:05
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.tt.lvruheng.eyepetizer.network;

public interface IOpenApiDataServiceCallback<T> {
	void onGetData(T data);

	void onGetError(int errorCode, String errorMsg, Throwable error);
}
