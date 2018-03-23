/*
 * 系统名称：lswuyou
 * 类  名  称：Respose.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0
 * 开发人员： huashigen
 * 开发时间： 2015-8-7 下午4:58:52
 * 功能说明： 请求返回数据基础类
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.network.respose;

public class Response {
	/**
	 * 200 : 当且仅当code==200时，本次请求操作成功 0 : 最常见的错误代码 400 : 非法请求（请求头非法
	 * /系统参数非法），原则上这个值不会出现在APP上（除非App代码有问题），只可能我们的接口被第三方非法访问 500 : 服务器发生异常 401
	 * :无法确认用户正常登录了
	 * （当前发送请求的用户不存在（userKey非法）/用户被封号/userSecret密钥改变/用户请求被伪造等错误），注意：客户端检查到这种状态
	 * ，应该主动退出登录 402 : 时间戳误差过大，服务器拒绝请求；客户端应当立即重新获取服务器系统时间，并重发此请求。
	 */

	/** 请求操作成功 */
	public static final int CODE_SUCCESS = 200;
	/** 请求操作失败 */
	public static final int CODE_FAIL = 0;
	/** 非法请求（请求头非法 /系统参数非法），原则上这个值不会出现在APP上（除非App代码有问题），只可能我们的接口被第三方非法访问 */
	public static final int CODE_ILLEGAL = 400;
	/** 服务器发生异常 */
	public static final int CODE_SERVER_ECEPTION = 500;
	/**
	 * 无法确认用户正常登录了（当前发送请求的用户不存在（userKey非法）/用户被封号/userSecret密钥改变/用户请求被伪造等错误），注意：
	 * 客户端检查到这种状态，应该主动退出登录
	 */
	public static final int CODE_USER_LOGOUT = 401;
	/** 时间戳误差过大，服务器拒绝请求；客户端应当立即重新获取服务器系统时间，并重发此请求。 */
	public static final int CODE_TIME_ILLEGAL = 402;
	/**
	 * 表明这个第三方账号，还没注册过或绑定过，则需要先注册，则跳转到“选择身份”页面，选择以后，用户点击“完成注册” ，请求接口使用第三方账号注册
	 */
	public static final int CODE_UNREGISTER = 302;

	/** 返回错误码 */
	public int code;

	// /** 返回数据 */
	// public String data;
	public String message;
}
