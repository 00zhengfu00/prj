/*
 * 系统名称：lswuyou
 * 类  名  称：Constant.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0
 * 开发人员： huashigen
 * 开发时间： 2015-8-4 上午10:30:18
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.common;

public final class Constant {
	/** 新浪微博APP_KEY，第三方应用应该使用自己的 APP_KEY 替换该 APP_KEY */
	public static final String WEIBO_APP_KEY = "4084778119";

	/**
	 * 新浪微博应用的回调页，第三方应用可以使用自己的回调页。
	 * 
	 * <p>
	 * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响， 但是没有定义将无法使用 SDK 认证登录。
	 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
	 * </p>
	 */
	public static final String REDIRECT_URL = "http://www.thelper.cn/";

	/** 访问新浪微博服务接口的地址 */
	public static final String API_SERVER = "https://api.weibo.com/2";
	/** 获取新浪微博用户数据 url */
	public static final String WEIBO_USERINFO_URL = "https://api.weibo.com/2/users/show.json";

	/**
	 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
	 * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
	 * 
	 * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
	 * 
	 * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
	 * 
	 * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
	 * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
	 */
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read," + "follow_app_official_microblog," + "invitation_write";

	/** 微信 APP_ID 替换为你的应用从官方网站申请到的合法appId和appSecret */
	public static final String WEIXIN_APP_ID = "wx5b818a39cf904b62";
	public static final String WEIXIN_APP_Secret = "f8a2e1bf2d056c325710495eec1bbc8d";
	/** 微信支付成功action */
	public static final String WEIXIN_PAY_FINISHED = "weixin_pay_finished";
	/** 微信获取访问token url */
	public static String TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
	/** 微信获取用户信息 url */
	public static String WEIXIN_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo";

	/** 其中APP_ID是分配给第三方应用的appid，类型为String。 */
	public static final String QQ_APP_ID = "1104714183";
	public static final String QQ_APP_KEY = "UPRQdNaYjSj2sqNG";

	/** 账户类sharedPreference name */
	public static final String ACCOUNT_PF = "account_pf";

	/** 字符编码 */
	public static String CHARACTER_ENCODING = "UTF-8";

	/** 登录类型 */
	public static final String ACCOUNT_TYPE = "account_type";
	public static final int ACCOUNT_TYPE_LSWY = 1;
	public static final int ACCOUNT_TYPE_QQ = 3;
	public static final int ACCOUNT_TYPE_WEIXIN = 2;
	public static final int ACCOUNT_TYPE_WEIBO = 4;

	/** 年级 */
	public static final int SUBJECT_GRADE_PRIMARY = 1;
	public static final int SUBJECT_GRADE_JUNIOR = 2;
	public static final int SUBJECT_GRADE_SENIOR = 3;

	/** 未选择 */
	public static final int UN_SELECTED = -1;

	/** 科目 */
	public static final int SUBJECT_TYPE_CHINESE = 11;
	public static final int SUBJECT_TYPE_MATHS = 12;
	public static final int SUBJECT_TYPE_ENGLISH = 13;
	public static final int SUBJECT_TYPE_PHYSICS = 14;
	public static final int SUBJECT_TYPE_POLITICS = 15;
	public static final int SUBJECT_TYPE_HISTORY = 16;
	public static final int SUBJECT_TYPE_GEOGRAPHY = 17;
	public static final int SUBJECT_TYPE_SOCIOLOGY = 18;
	public static final int SUBJECT_TYPE_BIOLOGY = 19;
	public static final int SUBJECT_TYPE_SCIENCE = 20;
	public static final int SUBJECT_TYPE_SXPD = 21;
	public static final int SUBJECT_TYPE_CHEMISTRY = 22;

	/** 登录状态验证码类型。0:注册;1：找回密码;2:绑定手机号 */
	public static final int TYPE_REGISTER = 0;
	public static final int TYPE_RECOVER_PASSWD = 1;
	public static final int TYPE_BIND_PHONE = 2;

	/** 用户类型 */
	public static final String USER_TYPE = "user_type";
	/** 用户类型-1-未知; 0-老师;1-学生; 2-家长 */
	public static final int USER_TYPE_UNDEFINED = -1;
	public static final int USER_TYPE_TEACHER = 0;
	public static final int USER_TYPE_STUDENT = 1;
	public static final int USER_TYPE_PARENT = 2;

	/** 区分进入更改手机号页面，还是设置手机号页面 */
	public static final int PAGE_RESET_PHONE = 0;
	public static final int PAGE_BIND_PHONE = 1;

	/** 班级类型 -当前班级 */
	public static final int CLASS_TYPE_CURRENT = 0;
	/** 班级类型 -历史班级 */
	public static final int CLASS_TYPE_HISTORY = 1;

	/** 班级类型 -历史班级 */
	public static final String REDIRECT_TYPE = "redirect_type";
	public static final int REDIRECT_MESSAGE = 0;
	public static final int REDIRECT_HOMEWORK_MANAGE = 1;


	//新手指引
	public static final String FRESHER_URL = "http://mp.weixin.qq.com/s?__biz=MzI0OTE5MDAwNg==&mid=505703770&idx=1&sn=700e61b242eafe42dc841afffe6d265d&scene=0&previewkey=XVFeSDhCiHGnBTXRAwF6q8c2LCzidIVXFV95LKkbHHY%3D#wechat_redirect";
	//联系我们
	public static final String CONTACT_URL = "http://mp.weixin.qq.com/s?__biz=MzI0OTE5MDAwNg==&mid=505703767&idx=1&sn=1c18c47db8646ec7bb3f3f7008dd335e&scene=0&previewkey=XVFeSDhCiHGnBTXRAwF6q8c2LCzidIVXFV95LKkbHHY%3D#wechat_redirect";
}
