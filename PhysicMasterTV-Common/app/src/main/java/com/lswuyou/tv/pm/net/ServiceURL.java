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
package com.lswuyou.tv.pm.net;

public final class ServiceURL {
    /**
     * 总的请求地址
     */
    public static final String URL_OFFICIAL = "http://api.tv.lswuyou.com";
//            public static final String URL = "https://d.thelper.cn/tv";
    public static final String URL = "http://192.168.1.31:8089";
//    public static final String URL = "http://m.thelper.cn";
//    public static final String URL = "http://sandbox.api.thelper.cn:8081";

    /**
     * 启动页广告接口
     **/
    public static final String GET_ADS_IMG = "/c/api/p/sys/startup0";

    /**
     * 获取首页数据接口-未登录
     */
    public static final String GET_HOME_PAGE_UNLOGINED = "/c/api/r/s/home/get";

    /**
     * 获取首页数据接口-已登录
     */
    public static final String GET_HOME_PAGE_LOGINED = "/c/api/a/s/home/get";

    /**
     * 获取登录配置url
     */
    public static final String GET_LOGIN_CFG_URL = "/c/api/r/f/account/get";

    /**
     * 微信登录
     */
    public static final String WEIXIN_LOGIN = "/c/api/r/f/account/loginByWx";

    /**
     * 获取用户账户详情
     */
    public static final String GET_USER_ACCOUNT = "/c/api/a/s/user/details";

    /**
     * 获取优惠课程详情-未登录
     */
    public static final String GET_PACK_COURSE_DETAIL_UNLOGIN = "/c/api/r/s/course/getPack";

    /**
     * 获取优惠课程详情-已登录
     */
    public static final String GET_PACK_COURSE_DETAIL_LOGINED = "/c/api/a/s/course/getPack";

    /**
     * 获取章节详情-未登录
     */
    public static final String GET_CHAPTER_DETAIL_UNLOGIN = "/c/api/r/s/chapter/details";

    /**
     * 获取章节详情-已登录
     */
    public static final String GET_CHAPTER_DETAIL_LOGINED = "/c/api/a/s/chapter/details";

    /**
     * 获取课程订单信息
     */
    public static final String GET_COURSE_ORDER = "/c/api/a/f/course/makeOrder";

    /**
     * 获取课程订单状态
     */
    public static final String GET_ORDER_STATUS = "/c/api/a/f/course/getOrderStatus";

    /**
     * 获取登录前视频详情
     */
    public static final String GET_VIDEO_DETAIL_UNLOGIN = "/c/api/r/s/video/details";

    /**
     * 获取登录后视频详情
     */
    public static final String GET_VIDEO_DETAIL_LOGIN = "/c/api/a/s/video/details";

    /**
     * 获取登录前视频播放
     */
    public static final String GET_VIDEO_PLAY_UNLOGIN = "/c/api/r/s/video/playV300";

    /**
     * 获取登录前视频播放
     */
    public static final String GET_VIDEO_PLAy_LOGIN = "/c/api/a/s/video/playV300";

    /**
     * 视频加入收藏
     */
    public static final String VIDEO_ADD_TO_FAV = "/c/api/a/s/video/favV3";

    /**
     * 视频取消收藏
     */
    public static final String VIDEO_CANCEL_FAV = "/c/api/a/s/video/unFavV3";

    /**
     * 获取新版本
     */
    public static final String GET_NEW_VERSION = "/c/api/p/sys/version";

    /**
     * 获取推荐课程
     */
    public static final String GET_RECOMMAND_COURSES = "/c/api/r/s/home/mostPopular";

    /**
     * 登录前根据拼音搜索课程
     */
    public static final String GET_UNLOGIN_SEARCH_COURSES = "/c/api/r/s/home/searchV3";

    /**
     * 登录后根据拼音搜索课程
     */
    public static final String GET_LOGINED_SEARCH_COURSES = "/c/api/a/s/home/searchV3";

    /**
     * 根据token验证登录是否成功
     */
    public static final String WEXIN_LOGIN_VERIFY = "/c/api/r/f/account/verifyToken";

    /**
     * 多渠道登录
     */
    public static final String LOGIN_BY_CHANNEL = "/c/api/r/f/account/loginByQd";

    /**
     * 渠道注册
     */
    public static final String REGISTER_BY_CHANNEL = "/c/api/r/f/account/registerByQd";

    /**
     * 微信绑定渠道
     */
    public static final String BINDQD = "/c/api/a/f/account/bindQd";

    /**
     * 根据科目ID获取章节列表-登录后
     */
    public static final String GET_CHAPTER_LIST_LOGINED = "/c/api/a/s/chapter/list";

    /**
     * 根据科目ID获取章节列表-登录前
     */
    public static final String GET_CHAPTER_LIST_UNLOGIN = "/c/api/r/s/chapter/list";

    /**
     * 获取精选列表-登录后
     */
    public static final String GET_JINGXUAN_LIST_LOGINED = "/c/api/a/s/chapter/choice";

    /**
     * 获取精选列表-登录前
     */
    public static final String GET_JINGXUAN_LIST_UNLOGIN = "/c/api/r/s/chapter/choice";

    /**
     * 获取会员列表详情接口
     */
    public static final String GET_MEMBER_LIST = "/c/api/a/s/member/list";

    /**
     * 获取收藏视频详情接口
     */
    public static final String GET_FAV_LIST = "/c/api/a/s/video/favList";

    /**
     * 获取登录二维码拼接字段
     */
    public static final String GET_BUY_QRCODE = "/c/api/r/f/account/getQrcode";

    /**
     * 获取验证二维码是否扫描登入接口
     */
    public static final String VERIFY_TOKEN = "/c/api/r/f/account/verifyTokenV3";

    /**
     * 换一批精选视频接口-登录后
     */
    public static final String ANOTHER_BATCH_LOGINED = "/c/api/a/s/chapter/anotherBatch";

    /**
     * 换一批精选视频接口-登录前
     */
    public static final String ANOTHER_BATCH_UNLOGIN = "/c/api/r/s/chapter/anotherBatch";
}
