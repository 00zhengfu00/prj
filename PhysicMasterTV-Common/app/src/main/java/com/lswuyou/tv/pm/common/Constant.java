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
package com.lswuyou.tv.pm.common;

public final class Constant {
    //bugly appkey
    public static final String APPKEY = "b3a072be61";
    /**
     * 账户类sharedPreference name
     */
    public static final String ACCOUNT_PF = "account_pf";

    /**
     * 字符编码
     */
    public static String CHARACTER_ENCODING = "UTF-8";

    /**
     * 登录类型
     */
    public static final String ACCOUNT_TYPE = "account_type";
    public static final int ACCOUNT_TYPE_LSWY = 1;
    public static final int ACCOUNT_TYPE_QQ = 3;
    public static final int ACCOUNT_TYPE_WEIXIN = 2;
    public static final int ACCOUNT_TYPE_WEIBO = 4;

    /**
     * 登录状态验证码类型。0:注册;1：找回密码;2:绑定手机号
     */
    public static final int TYPE_REGISTER = 0;
    public static final int TYPE_RECOVER_PASSWD = 1;
    public static final int TYPE_BIND_PHONE = 2;

    public static final String CALLBACK_URL = "http://m.lswuyou.com/c/p/callback/weixinscan";

    public static final String[] videoQualities = {"流畅", "标清", "高清", "超清"};
    public static final String[] videoQualitiesTag = {"LD", "SD", "HD", "FHD"};

    //物理
    public static final int SUB_PHYSICS = 1;
    //化学
    public static final int SUB_CHEMISTRY = 2;
    //数学
    public static final int SUB_MATH = 4;
}
