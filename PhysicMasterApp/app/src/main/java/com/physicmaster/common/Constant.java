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
package com.physicmaster.common;

public final class Constant {
    public static final String PHYSICMASTER = "com.physicmaster";
    public static final String CHYMISTMASTER = "com.lswuyou.chymistmaster";
    public static final String MATHMASTER = "com.lswuyou.mathmaster";

    public static final String KEYSTORE_PWD = "LSWY%ad51";
    /**
     * 新浪微博应用的回调页，第三方应用可以使用自己的回调页。
     * <p>
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响， 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String WEIBO_REDIRECT_URL = "http://www.sina.com";

    /**
     * 访问新浪微博服务接口的地址
     */
    public static final String API_SERVER = "https://api.weibo.com/2";
    /**
     * 获取新浪微博用户数据 url
     */
    public static final String WEIBO_USERINFO_URL = "https://api.weibo.com/2/users/show.json";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
     * <p>
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
     * <p>
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * <p>
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read," +
            "follow_app_official_microblog," + "invitation_write";
    /**
     * 微信支付成功action
     */
    public static final String WEIXIN_PAY_FINISHED = "weixin_pay_finished";
    /**
     * 微信获取访问token url
     */
    public static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
    /**
     * 微信刷新token url
     */
    public static String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
    /**
     * 微信获取用户信息 url
     */
    public static String WEIXIN_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo";

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
    public static final String NETTYPE_WIFI = "WIFI";
    public static final String NETTYPE_2G = "2G";
    public static final String NETTYPE_3G = "3G";
    public static final String NETTYPE_4G = "4G";
    public static final String NETTYPE_UNKNOWN = "unknown";
    public static final String NETTYPE_UNCONNECTED = "unconnected";

    //新手指引
    public static final String FRESHER_URL = "http://mp.weixin.qq" +
            ".com/s?__biz=MzI0OTE5MDAwNg==&mid=505703770&idx=1&sn" +
            "=700e61b242eafe42dc841afffe6d265d" +
            "&scene=0&previewkey=XVFeSDhCiHGnBTXRAwF6q8c2LCzidIVXFV95LKkbHHY%3D#wechat_redirect";
    //联系我们
    public static final String CONTACT_URL = "http://mp.weixin.qq" +
            ".com/s?__biz=MzI0OTE5MDAwNg==&mid=505703767&idx=1&sn" +
            "=1c18c47db8646ec7bb3f3f7008dd335e&scene=0&previewkey" +
            "=XVFeSDhCiHGnBTXRAwF6q8c2LCzidIVXFV95LKkbHHY%3D#wechat_redirect";

    //物理大师-新浪微博
    public static final String PM_WEIBO_APP_KEY = "35497744";
    public static final String PM_WEIBO_APP_SECRET = "9ebb43fe391fa2bd761ac77f3452f594";
    //化学大师-新浪微博
    public static final String CM_WEIBO_APP_KEY = "2769985964";
    public static final String CM_WEIBO_APP_SECRET = "a715d8952668588641958e7bfe56ff72";
    //数学大师-新浪微博
    public static final String MM_WEIBO_APP_KEY = "1520914257";
    public static final String MM_WEIBO_APP_SECRET = "dbfc22b78c48c4b12447eecd53051cbc";

    //物理大师-阿里百川
    public final static String PM_ALIBAICHUAN_APPKEY = "23640497";
    //化学大师-阿里百川
    public final static String CM_ALIBAICHUAN_APPKEY = "23640656";
    //数学大师-阿里百川
    public final static String MM_ALIBAICHUAN_APPKEY = "23639691";

    //物理大师-bugly
    public final static String PM_BUGLY_APPKEY = "f843ed5803";
    //化学大师-bugly
    public final static String CM_BUGLY_APPKEY = "28bd2769c6";
    //数学大师-bugly
    public final static String MM_BUGLY_APPKEY = "116819af3a";


    //物理大师-小米推送
    public static final String PM_XIAOMI_APP_ID = "2882303761517464759";
    public static final String PM_XIAOMI_APP_KEY = "5331746465759";
    //化学大师-小米推送
    public static final String CM_XIAOMI_APP_ID = "2882303761517509886";
    public static final String CM_XIAOMI_APP_KEY = "5341750939886";
    //数学大师-小米推送
    public static final String MM_XIAOMI_APP_ID = "2882303761517563648";
    public static final String MM_XIAOMI_APP_KEY = "5611756339648";

    //物理大师-qq相关
    public static final String PM_QQ_APP_ID = "1105352028";
    public static final String PM_QQ_APP_KEY = "OlezoB9spLqdvoQp";
    //化学大师-qq相关
    public static final String CM_QQ_APP_ID = "1105344977";
    public static final String CM_QQ_APP_KEY = "OGO4eMR4zwQhLjPZ";
    //数学大师-qq相关
    public static final String MM_QQ_APP_ID = "1105846265";
    public static final String MM_QQ_APP_KEY = "B5nhTRxAuRuVaAw9";


    //物理大师-微信相关
    public static final String PM_WEIXIN_APP_ID = "wxe88d090c7b606abb";
    public static final String PM_WEIXIN_APP_SECRET = "9d31e0f8cf691a5f6798c5325095e358";
    //化学大师-微信相关
    public static final String CM_WEIXIN_APP_ID = "wxfb1e2af898432556";
    public static final String CM_WEIXIN_APP_SECRET = "8bb5b31000fefd46111342911d58bda1";
    //数学大师-微信相关
    public static final String MM_WEIXIN_APP_ID = "wx4eaafa141171ac67";
    public static final String MM_WEIXIN_APP_SECRET = "0e500e255ea0022dd94e4fe5fc812ead";

    //app分包科目类型id对应关系
    public static final int SUBJECTTYPE_PM = 51;
    public static final int SUBJECTTYPE_CM = 52;
    public static final int SUBJECTTYPE_EM = 53;
    public static final int SUBJECTTYPE_MM = 54;
    public static final int SUBJECTTYPE_BM = 55;

    //subjectId
    public static final int SUBJECTID_PM = 1;
    public static final int SUBJECTID_CM = 2;
    public static final int SUBJECTID_MM = 4;
    public static final int SUBJECTID_HPM = 5;
    public static final int SUBJECTID_NS = 3;

    //视频清晰度
    public static final String FHD = "FHD";//全高清-534kb/9s-网速需求：59kb/s
    public static final String HD = "HD";//高清-266kb/7s-网速需求：38kb/s
    public static final String SD = "SD";//标清-251kb/9s-网速需求：27kb/s
    public static final String LD = "LD";//流畅-210kb/9s-网速需求：23kb/s
}
