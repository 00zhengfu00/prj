/*
 * 系统名称：lswuyou
 * 类  名  称：ErrorCode.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0
 * 开发人员： huashigen
 * 开发时间： 2015-8-7 下午4:41:25
 * 功能说明： 错误码定义
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.physicmaster.net;

public class ErrorCode {
	/** 连接超时 */
	public static final int CONNECT_TIMEOUT = -96;
	/** socket */
	public static final int SOCKET_TIMEOUT = -97;
	public static final int SOCKET_ERROR = -98;
	public static final int RUNTIME_ERROR = -99;
	public static final int ON_SUCCESS_ERROR = -100;

	/** 错误消息 */
	public static final String TIMEOUT_ERROR = "连接超时，请检查网络连接！";
	public static final String SERVICE_UNAVAILABLE = "服务暂不可使用！";
	public static final String RETURN_ERROR = "网络错误！";
}
