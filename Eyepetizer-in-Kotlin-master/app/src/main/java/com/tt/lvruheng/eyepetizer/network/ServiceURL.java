/*
 * 系统名称：lswuyou
 * 类  名  称：ServiceURL.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0
 * 开发人员： huashigen
 * 开发时间： 2015-8-10 上午9:41:47
 * 功能说明：
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.tt.lvruheng.eyepetizer.network;

public final class ServiceURL {
    /**
     * 总的请求地址
     */
    //侬享分 地址http://192.168.1.128:5656/#/loan/loan/backHouse
    //享普惠 地址 http://192.168.1.128:5656/#/loan/loan/house

    //测试环境
//    public static final String URL = "http://nxphwap.51helpme.com/";

    //正式环境
    public static final String URL = "https://wap.51helpme.com/";

    //public static final String URL = "http://192.168.1.128:5656/";

    public static final String URL_API = URL + "api/";

    //用户是否注册
    public static final String IS_REGISTERED = "login/is_registered";
    //注册接口
    public static final String SIGNON = "login/signon";
    //获取图形验证码
    public static final String GET_CAPTCHA = "login/createRegistImage";
    //用户登录
    public static final String LOGIN = "login/login";
    //忘记密码
    public static final String FIND_PASSWD = "login/find_password";
    //验证图形验证码
    public static final String LOGOUT = "login/logout";
    //用户退出登录
    public static final String CHECK_IMG_CODE = "login/checkegistValidCode";
    //身份证ocr识别
    public static final String IDCARD_OCR = "authRequest/idCardOcr";
    //是否实名认证
    public static final String ID_IDENTITY_AUTH = "authRequest/is_identityAuth";
    //姓名身份证号码联网查询认证
    public static final String CHECK_ID_AND_NAME = "authRequest/checkCIdAndName";
    //修改密码
    public static final String CHANGE_PASSWD = "login/change_passwd";
    //版本更新
    public static final String VERSION_UPDATE = "app/checkVersion";
    //获取实名认证信息
    public static final String GET_IDENTITY = "authRequest/getInfo";
}
