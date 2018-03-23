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
package com.physicmaster.net;

public final class ServiceURL {
    /**
     * 总的请求地址
     */
    public static final String URL_OFFICIAL = "http://api.app.lswuyou.com";
//    public static final String URL = "https://d.thelper.cn/mobile-api";
    //public static final String URL = "http://test.api.d.thelper.cn";
//    public static final String URL = "http://192.168.1.30:8090";
    public static final String URL = "http://192.168.1.31:8090";
    public static String DYNAMIC_URL = "";
    /**
     * 获取访问协议
     */
    public static final String PM_PROTOCOL_URL = "https://cdn.thelper.cn/app_ctrl/pm/a_3.2.1.json";
    public static final String CM_PROTOCOL_URL = "https://cdn.thelper.cn/app_ctrl/cm/a_3.2.1.json";
    public static final String MM_PROTOCOL_URL = "https://cdn.thelper.cn/app_ctrl/mm/a_3.2.1.json";
    //    public static final String PM_PROTOCOL_URL = "https://www.lswuyou.com/c1.json";

    /**
     * 手机号登录
     */
    public static final String LOGIN_BYPHONE = "/c/api/r/f/user/login";

    /**
     * App用户第一次引导接口
     */

    public static final String LOGIN_INITIAL = "/c/api/a/f/user/initialV3";
    /**
     * 获取当前时间
     */
    public static final String GETTIME = "/c/api/p/sys/getTime";

    /**
     * 获取验证码
     */
    public static final String GET_VERIFICATION = "/c/api/r/f/user/sendVerifyCode";

    /**
     * 校验验证码
     */
    public static final String VERIFY_CODE = "/c/api/r/f/user/verifyCode";

    /**
     * 手机号码注册
     */
    public static final String REGISTER = "/c/api/r/f/user/register";

    /**
     * 通过手机验证重置登录密码
     */
    public static final String RECOVER_PASSWD_PHONE = "/c/api/r/f/user/resetPasswd";

    /**
     * 通过原密码重置登录密码
     */
    public static final String RECOVER_PASSWD_OPWD = "/c/api/a/s/user/changePasswd";

    /**
     * 退出登录
     */
    public static final String LOGOUT = "/c/api/a/f/user/logout";

    /**
     * 修改用户信息
     */
    public static final String UPDATE_USERINFO = "/c/api/a/f/user/updateUserInfo";

    /**
     * 获取区域下的学校数据
     */
    public static final String FIND_SCHOOL = "/c/api/a/s/user/findSchool";

    /**
     * 修改区域和学校
     */
    public static final String UPDATE_USER_SCHOOL = "/c/api/a/f/user/updateUserSchool";

    /**
     * 登录前启动接口
     */
    public static final String STARTUP_UNLOGIN = "/c/api/r/f/sys/startup";

    /**
     * 登录后启动接口
     */
    public static final String STARTUP_LOGINED = "/c/api/a/f/sys/startup";

    /**
     * 课程首页接口
     */
    public static final String COURSE_INDEX = "/c/api/a/s/course/index";

    /**
     * 获取阿里云OSS访问Token接口
     */
    public static final String GET_OSS_TOKEN = "/c/api/a/f/user/getOssToken";

    /**
     * 获取阿里云服务器信息接口
     */
    public static final String GET_ALIYUN_SERVER_INFO = "/c/api/a/f/sys/getOssCfg";

    /**
     * 获取阿里云服务器信息和ossToken接口
     */
    public static final String GET_OSS_INFO = "/c/api/a/f/user/getOssInfo";

    /**
     * 获取用户基本信息
     */
    public static final String GET_USER_INFO = "/c/api/a/f/user/getMyInfo";

    /**
     * 获取课程详情
     */
    public static final String GET_COURSE_DETAIL = "/c/api/a/s/course/detail";

    /**
     * 课程预下单接口
     */
    public static final String COURSE_ORDER = "/c/api/a/f/order/makeOrder";

    /**
     * 课程下单接口
     */
    public static final String PREPAY = "/c/api/a/f/order/prepay";

    /**
     * 订单查询接口
     */
    public static final String CHECK_ORDER = "/c/api/a/f/order/getOrderStatus";
    /**
     * 引导宠物接口
     */
    public static final String SHOW_PET = "/c/api/a/s/pet/list";

    /**
     * 好友邀请码接口
     */
    public static final String INVITE_CODE = "/c/api/a/f/user/setFriendInviteCode";

    /**
     * 第三方账号登入接口
     */
    public static final String LOGIN_BY_OPEN = "/c/api/r/f/user/loginByOpen";

    /**
     * 宠物信息接口
     */
    public static final String PET_INFO = "/c/api/a/s/pet/detail";

    /**
     * 排行榜接口
     */
    public static final String RANK = "/c/api/a/s/user/rank";

    /**
     * 好友搜索接口
     */
    public static final String FIND_FRIEND = "/c/api/a/s/friend/search";

    /**
     * 获取好友列表接口
     */
    public static final String GET_FRIEND_LIST = "/c/api/a/s/friend/getList";

    /**
     * 请求添加好友接口
     */
    public static final String INVITE_FRIEND = "/c/api/a/s/friend/relationInvitation";

    /**
     * 获取好友申请列表接口
     */
    public static final String GET_INVITATION_LIST = "/c/api/a/s/friend/getRelationList";

    /**
     * 同意好友申请
     */
    public static final String AGREE_INVITATION = "/c/api/a/s/friend/updateRelationStatus";

    /**
     * 获取好友信息详情
     */
    public static final String GET_FRIEND_DETAIL = "/c/api/a/s/friend/getUserDetail";

    /**
     * 删除好友
     */
    public static final String DELETE_FRIEND = "/c/api/a/s/friend/deleteRelation";

    /**
     * 习题闯关获取课程详情接口
     */
    public static final String COURSE_EXCERCISE = "/c/api/a/s/study/getCourse";

    /**
     * 习题闯关视频播放详情接口
     */
    public static final String VIDEO_PLAY = "/c/api/a/s/study/getVideo";

    /**
     * 获取备忘接口
     */
    public static final String MEMO_LIST = "/c/api/a/s/user/memoList";

    /**
     * 获取错题接口
     */
    public static final String QUESTION_WRONG = "/c/api/a/s/question/wrongBook";

    /**
     * 获取课程收藏接口
     */
    public static final String VIDEO_COLLECTION = "/c/api/a/s/video/favList";

    /**
     * 获取习题闯关接口
     */
    public static final String EXPLORE = "/c/api/a/s/study/explore";

    /**
     * 提交习题答案
     */
    public static final String COMMIT_ANWSER = "/c/api/a/s/question/submit";
    /**
     * 首页信息接口
     */
    public static final String INDEX = "/c/api/a/s/study/index";

    /**
     * 收藏视频
     */
    public static final String COLLECT_VIDEO = "/c/api/a/s/video/fav";

    /**
     * 取消收藏视频
     */
    public static final String UN_COLLECT_VIDEO = "/c/api/a/s/video/unFav";

    /**
     * 金币商城接口
     */
    public static final String MALL = "/c/api/a/s/gold/mall";
    /**
     * 删除备忘接口
     */
    public static final String DELETE_MEMO = "/c/api/a/s/user/deleteMemo";
    /**
     * 加入备忘接口
     */
    public static final String ADD_MEMO = "/c/api/a/s/user/addMemo";
    /**
     * 获取任务接口
     */
    public static final String TASK_LIST = "/c/api/a/s/mission/list";
    /**
     * 获取任务接口
     */
    public static final String BUY_PROP = "/c/api/a/s/gold/buyProp";
    /**
     * 金币流水记录接口
     */
    public static final String TRANSACTION = "/c/api/a/s/gold/transaction";

    /**
     * 视频播放插入log记录接口
     */
    public static final String VIDEO_PLAY_LOG = "/c/api/a/s/video/playLog";
    /**
     * 获取好友免费精力接口
     */
    public static final String GET_INVITE_ENERGY = "/c/api/a/s/friend/getInviteEnergyList";
    /**
     * 索要精力接口
     */
    public static final String REQUEST_ENERGY = "/c/api/a/s/friend/askForEnergy";
    /**
     * 背包道具接口
     */
    public static final String BACKPACK = "/c/api/a/s/prop/pack";
    /**
     * 使用道具接口
     */
    public static final String USE_PRPO = "/c/api/a/s/prop/use";
    /**
     * 用户求精力接口
     */
    public static final String REFUSE_ENERGY_LIST = "/c/api/a/s/friend/getEnergyList";
    /**
     * 用户同意领取或者赠送精力接口
     */
    public static final String RECEIVE_ENERGY = "/c/api/a/s/friend/receiveEnergyV210";
    /**
     * 用户拒绝领取或者赠送精力接口
     */
    public static final String REFUSE_ENERGY = "/c/api/a/s/friend/refuseEnergy";
    /**
     * 首页获取课程列表接口
     */
    public static final String COURSE_LIST = "/c/api/a/s/study/getCourseList";
    /**
     * 领取任务奖励接口
     */
    public static final String GET_AWARD = "/c/api/a/s/mission/award";
    /**
     * 精力详情接口
     */
    public static final String ENERGY = "/c/api/a/s/pet/energy";
    /**
     * 我的课程接口
     */
    public static final String MY_COURSE = "/c/api/a/s/user/getMyCourseV3";
    /**
     * 我的课程接口
     */
    public static final String FEED_BACK = "/c/api/a/s/user/feedback";
    /**
     * 获取分享数据接口
     */
    public static final String GET_SHARED_DATA = "/c/api/a/s/user/getShareData";
    /**
     * 分享成功接口
     */
    public static final String SHARED_SUCCESS = "/c/api/a/s/user/shareFinish";
    /**
     * 用户领取习题闯关进阶关卡礼包接口
     */
    public static final String PROGRESS_AWARD = "/c/api/a/s/chapter/progressAward";
    /**
     * 获取错题本下的所有习题接口
     */
    public static final String QUESTION_WRONG_LIST = "/c/api/a/s/user/getQuestionWrongList";
    /**
     * App消灭错题接口
     */
    public static final String ELIMINATE_WRONG = "/c/api/a/s/question/eliminateWrong";
    /**
     * 提交错题本答案接口
     */
    public static final String SUBMIT_QUESTION_WRONG = "/c/api/a/s/question/submitWrong";

    /**
     * 用户获取新鲜事接口
     */
    public static final String FRESH_NEWS = "/c/api/a/s/user/getFreshNews";

    /**
     * 用户忽略新鲜事接口
     */
    public static final String IGNORE_FRESH_NEWS = "/c/api/a/s/user/ignoreFreshNews";
    /**
     * 修改密码校验旧密码接口
     */
    public static final String VERIFT_PASSWD = "/c/api/a/f/user/verifyPasswd";

    /**
     * 绑定手机号发送验证码接口
     */
    public static final String BIND_PHONE_VERIFY = "/c/api/a/f/user/bindCellphoneSendVerifyCode";
    /**
     * 绑定手机号接口
     */
    public static final String BIND_PHONE = "/c/api/a/f/user/bindCellphone";
    /**
     * 第三方绑定
     */
    public static final String BIND_OPEN = "/c/api/a/f/user/bindByOpen";
    /**
     * 宠物进阶借口
     */
    public static final String UP_STAGE = "/c/api/a/s/pet/upStage";

    /**
     * App获取宠物努力值接口
     */
    public static final String PET_EFFORT = "/c/api/a/s/pet/effort";

    /**
     * App使用道具接口
     */
    public static final String USE_ENDEAVOR = "/c/api/a/s/prop/use";

    /**
     * App获取签到数据接口
     */
    public static final String GET_PLAN_LIST = "/c/api/a/s/checkIn/getPlanList";

    /**
     * App签到接口
     */
    public static final String SIGN_IN = "/c/api/a/s/checkIn/today";

    /**
     * App补签到接口
     */
    public static final String SIGN_IN_AFTER = "/c/api/a/s/checkIn/remedy";

    /**
     * App web授权登入接口
     */

    public static final String GET_AUTH_CODE = "/c/api/a/f/user/getAuthCode";
    /**
     * App通过网页方式QQ登录
     */
    public static final String LOGIN_BY_QQWEB = "/c/api/r/f/user/loginByQqWeb";

    /**
     * App通过网页方式绑定
     */
    public static final String BIND_BY_QQWEB = "/c/api/a/f/user/bindQqWeb";

    /**
     * App获取视频下载相关信息
     */
    public static final String GET_DOWNLOAD_INFO = "/c/api/a/f/video/download";

    /**
     * App课程代付分享日志接口
     */
    public static final String HELP_PAY_LOG = "/c/api/a/s/course/helpPayLog";

    /**
     * 发送异常到服务端
     */
    public static final String SEND_LOG = "/c/api/r/s/log/aes";

    /**
     * 新版习题闯关
     */
    public static final String EXPLOREV2 = "/c/api/a/s/question/explore";
    /**
     * 新版错题本
     */
    public static final String GET_WRONG_QU_LIST_V2 = "/c/api/a/s/question/wrongList";
    /**
     * 学习信息接口
     */
    public static final String STUDY_INFO = "/c/api/a/s/stat/studyInfo";
    /**
     * 我的勋章列表接口
     */
    public static final String MEDAL_LIST = "/c/api/a/s/medal/list";
    /**
     * 检查是否存在获取新的勋章接口
     */
    public static final String CHECK_MEDAL = "/c/api/a/s/medal/check";


    /**
     * App问答圈提问列表
     */
    public static final String QUESTION_LIST = "/c/api/a/s/qaQuestion/list";

    /**
     * App我的提问列表
     */
    public static final String QUESTION_MYLIST = "/c/api/a/s/qaQuestion/myList";

    /**
     * App问答圈发布提问
     */
    public static final String QUESTION_RELEASE = "/c/api/a/s/qaQuestion/release";
    /**
     * App问答圈删除提问接口
     */
    public static final String QUESTION_DELETE = "/c/api/a/s/qaQuestion/delete";
    /**
     * App问答圈提问详情
     */
    public static final String QUESTION_DETAIL = "/c/api/a/s/qaQuestion/detail";
    /**
     * App问答圈提问详情中回答列表分页接口
     */
    public static final String ANSWER_LIST = "/c/api/a/s/qaQuestion/answerList";
    /**
     * App问答圈问题回答接口
     */
    public static final String ANSWER_RELEASE = "/c/api/a/s/qaAnswer/release";
    /**
     * App问答圈删除我的回答接口
     */
    public static final String ANSWER_DELETE = "/c/api/a/s/qaAnswer/delete";
    /**
     * App问答圈问题回答详情接口
     */
    public static final String ANSWER_DELAIL = "/c/api/a/s/qaAnswer/detail";
    /**
     * App问答圈我的回答列表接口
     */
    public static final String ANSWER_MYLIST = "/c/api/a/s/qaAnswer/getMyList";
    /**
     * App问答圈评论回答接口
     */
    public static final String ANSWER_COMMENT = "/c/api/a/s/qaAnswer/comment";
    /**
     * App问答圈删除评论接口
     */
    public static final String ANSWER_DELETE_COMMENT = "/c/api/a/s/qaAnswer/deleteComment";
    /**
     * App问答圈回答评论列表接口
     */
    public static final String ANSWER_COMMENT_LIST = "/c/api/a/s/qaAnswer/commentList";
    /**
     * App问答圈回答点赞接口
     */
    public static final String ANSWER_LIKE = "/c/api/a/s/qaAnswer/like";
    /**
     * App问答圈回答取消点赞接口
     */
    public static final String ANSWER_UNLIKE = "/c/api/a/s/qaAnswer/unlike";
    /**
     * App问答圈消息事件列表接口
     */
    public static final String MESSAGE_LIST = "/c/api/a/s/qaAnswer/newsList";
    /**
     * App问答圈忽略消息接口
     */
    public static final String MESSAGE_NEWS = "/c/api/a/s/qaAnswer/markNews";
    /**
     * App问答圈讨论banner
     */
    public static final String DISCUSS_BANNER = "/c/api/a/s/qaQuestion/banner";
    /**
     * 好友搜索版本2.0
     */
    public static final String FIND_FRIEND_V2 = "/c/api/a/s/friend/searchV2";
    /**
     * 批量通过手机号搜索好友接口
     */
    public static final String FIND_FRIEND_BATCH = "/c/api/a/f/friend/search";
    /**
     * 通过dtUserId获取用户基础信息接口
     */
    public static final String GET_OTHER_USER_INFO = "/c/api/a/s/friend/getUser";
    /**
     * 通过dtUserId获取用户基础信息接口
     */
    public static final String GET_OTHER_USER_INFO_BY_IM = "/c/api/a/s/friend/getUserIm";
    /**
     * App检查该app在此设备是否上传过手机通讯录接口
     */
    public static final String CHECK_ADDRESS_LIST_UPLOAD_STATE = "/c/api/a/s/ab/c";
    /**
     * App上传手机通讯录接口
     */
    public static final String ADDRESS_LIST_UPLOAD = "/c/api/a/s/ab/u";
    /**
     * 获取系统推荐的好友
     */
    public static final String RECOMMEND_FRIENDS = "/c/api/a/s/friend/recommend";
    /**
     * 问答圈用户投诉接口
     */
    public static final String REPORT = "/c/api/a/s/qaQuestion/complaint";
    /**
     * 游客登录
     */
    public static final String TOURIST_LOGIN = "/c/api/r/f/user/tourist";
    /**
     * 排行榜
     */
    public static final String RANK_DATA = "/c/api/a/s/rank/detail";
    /**
     * 学习首页获取章节详情接口
     */
    public static final String CHAPTER_DETAILS = "/c/api/a/s/chapter/detail";
    /**
     * 学习首页获取章节列表接口
     */
    public static final String CHAPTER_LIST = "/c/api/a/s/chapter/listV310";
    /**
     * 获取视频详情
     */
    public static final String VIDEO_DETAILS = "/c/api/a/s/video/detail";

    /**
     * App用户年级科目修改接口
     */
    public static final String UPDATA_GRADE_SUBJECT = "/c/api/a/f/user/changeBook";

    /**
     * 探索首页接口
     */
    public static final String EXPLORE_INDEX = "/c/api/a/s/explore/index";
    /**
     * 获取用户会员列表接口
     */
    public static final String MEMBERS_LIST = "/c/api/a/s/member/list";
    /**
     * 探索会员专区接口
     */
    public static final String EXPLORE_MEMBER = "/c/api/a/s/explore/member";
    /**
     * 探索播放最新视频接口
     */
    public static final String NEW_VIDEO = "/c/api/a/s/explore/playNewVideo";
    /**
     * 点击一个课程跳转到首页接口
     */
    public static final String COURSE2LIST = "/c/api/a/s/chapter/course2list";
    /**
     * 通过扫描必练册题目二维码获取对应的视频信息接口
     */
    public static final String XITI_QR = "/c/api/a/s/video/xitiByQr";
    /**
     * 缓存视频下载接口
     */
    public static final String VIDEO_DOWNLOAD = "/c/api/a/f/video/downloadV3";
    /**
     * 会员下订单接口
     */
    public static final String MEMBER_MAKE = "/c/api/a/f/member/order/make";
    /**
     * 会员获取订单详情接口
     */
    public static final String MEMBER_DETAILS = "/c/api/a/f/member/order/details";
    /**
     * 会员订单预支付接口
     */
    public static final String MEMBER_PREPAY = "/c/api/a/f/member/order/prepay";
    /**
     * 会员订单查询状态接口
     */
    public static final String MEMBER_STATUS = "/c/api/a/f/member/order/status";
    /**
     * 检查缓存视频是否有权限播放接口
     */
    public static final String CHECK_AUTH = "/c/api/a/f/video/check";
    /**
     * 收藏视频播放接口
     */
    public static final String LIKE_VIDEO_PLAY = "/c/api/a/s/video/playOnly";
    /**
     * 网络预支付
     */
    public static final String WEB_PREPAY = "/c/api/a/f/pay/prepay";

    /**
     * 用于在首页获取用户正在学习的 科目/教材版本/课本/章节等信息
     */
    public static final String STUDYING_INFO = "/c/api/a/s/chapter/info";

    /**
     * App错题本添加标签目录接口-/c/api/a/s/wb/addDir
     */
    public static final String ADD_TAG = "/c/api/a/s/wb/addDir";

    /**
     * App获取错题本列表接口-/c/api/a/s/wb/getDir
     */
    public static final String GET_DIR = "/c/api/a/s/wb/getDir";

    /**
     * App错题本获取错误原因接口
     */
    public static final String GET_WRONG_WHY = "/c/api/a/s/wb/getTag";

    /**
     * App错题本获取标签目录接口
     */
    public static final String GET_ALL_DIR = "/c/api/a/s/wb/getNormalDir";
    /**
     * App错题本录入错题接口
     */
    public static final String RECORD_WRONG = "/c/api/a/s/wb/recordWrong";
    /**
     * App错题本录入系统错题接口
     */
    public static final String RECORD_SYS_WRONG = "/c/api/a/s/wb/recordSysWrong";
    /**
     * App查询当前用户所有的错题原因及错题目录信息
     */
    public static final String INITWRONGPOOL = "/c/api/a/s/wb/initWrongPool";
    /**
     * App查询错题池中的错题列表接口
     */
    public static final String GETPOOLQULIST = "/c/api/a/s/wb/getPoolQuList";
    /**
     * App错题目录初始化数据接口
     */
    public static final String INITWRONGDIR = "/c/api/a/s/wb/initWrongDir";
    /**
     * 查询错题目录中的错题列表数据
     */
    public static final String GETDIRQULIST = "/c/api/a/s/wb/getDirQuList";
    /**
     * 从错题目录删除错题接口
     */
    public static final String RM_QU_FROM_DIR = "/c/api/a/s/wb/rmFromDir";
    /**
     * 将错题目录中的错题归档
     */
    public static final String ARCHIVE_QU_FROM_DIR = "/c/api/a/s/wb/archivalQu";
    /**
     * 将错题目录中的错题归档
     */
    public static final String RM_QU_FROM_POOL = "/c/api/a/s/wb/rmFromPool";
    /**
     * 删除错题目录接口
     */
    public static final String RM_QU_DIR = "/c/api/a/s/wb/rmDir";
    /**
     * 获取当前用户所有科目的错题池数量
     */
    public static final String GET_POOL_COUNT = "/c/api/a/s/wb/getPoolCount";
}
