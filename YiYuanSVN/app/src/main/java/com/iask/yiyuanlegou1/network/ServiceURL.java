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
package com.iask.yiyuanlegou1.network;

public final class ServiceURL {
    /**
     * 总的请求地址
     */
    public static final String URL_OFFICIAL = "http://api.lswuyou.cn";
    //    public static final String URL = "http://192.168.1.122:8081";
    //    public static final String URL = "http://192.168.1.113:8081";
    public static final String URL = "http://dev.lswuyou.cn:8100";
    /**
     * 登录
     */
    public static final String LOGIN = "/c/rest/r/user/login";

    /**
     * 获取当前时间
     */
    public static final String GETTIME = "/c/rest/p/sys/getTimeMillis";

    /**
     * 第三方登录
     */
    public static final String OPEN_LOGIN = "/c/rest/r/user/loginByOpen";

    /**
     * 第三方注册
     */
    public static final String OPEN_REGISTER = "/c/rest/r/user/registerByOpen";

    /**
     * 获取验证码
     */
    public static final String GET_VERIFICATION = "/c/rest/r/user/sendVerifyCode";

    /**
     * 登录状态下获取验证码
     */
    public static final String GET_VERIFICATION_LOGIN = "/c/rest/a/f/me/sendVerifyCode";

    /**
     * 校验验证码
     */
    public static final String VERIFY_CODE = "/c/rest/r/user/sendVerifyCode";

    /**
     * 登录状态下校验验证码
     */
    public static final String VERIFY_CODE_LOGIN = "/c/rest/a/f/me/verifyCode";

    /**
     * 手机号码注册
     */
    public static final String REGISTER = "/c/rest/r/user/register";

    /**
     * 重置登录密码
     */
    public static final String RECOVER_PASSWD = "/c/rest/r/user/resetPasswd";

    /**
     * 退出登录
     */
    public static final String LOGOUT = "/c/rest/a/f/me/logout";

    /**
     * 设置或修改密码
     */
    public static final String RESET_PASSWD = "/c/rest/r/user/resetPwd";

    /**
     * 获取用户信息
     */
    public static final String GET_USER_INFO = "/c/rest/a/f/me/getMyInfo";

    /**
     * 绑定手机号码
     */
    public static final String BIND_PHONE = "/c/rest/r/userInfo/bindPhone";

    /**
     * 解绑手机号码
     */
    public static final String UNBIND_PHONE = "/c/rest/a/f/me/unbindCellphone";

    /**
     * 绑定第三方账号
     */
    public static final String BIND_OPEN = "/c/rest/a/f/me/bindOpen";

    /**
     * 解绑第三方账号
     */
    public static final String UNBIND_OPEN = "/c/rest/a/f/me/unbindOpen";

    /**
     * 修改无忧号
     */
    public static final String EDIT_WYID = "/c/rest/a/f/me/changeWyId";

    /**
     * 修改用户信息
     */
    public static final String UPDATE_USERINFO = "/c/rest/a/f/me/updateUserInfo";

    /**
     * 启动时获取账户信息
     */
    public static final String STARTUP0 = "/c/rest/p/sys/startup0";

    /**
     * 启动时获取账户信息
     */
    public static final String STARTUP1 = "/c/rest/a/f/me/startup1";

    /**
     * 获取osstoken
     */
    public static final String OSSTOKEN_GET = "/c/rest/r/home/getOssToken";

    /**
     * 获取区域下的学校数据
     */
    public static final String FIND_SCHOOL = "/c/rest/a/s/me/findSchool";

    /**
     * 修改区域和学校
     */
    public static final String UPDATE_USER_SCHOOL = "/c/rest/a/f/me/updateUserSchool";

    /**
     * 用户登录状态下设置密码
     */
    public static final String SET_PASSWORD = "/c/rest/a/f/me/setPasswd";

    /**
     * 用户重置手机号码
     */
    public static final String RESET_PHONE = "/c/rest/a/f/me/changeCellphone";

    /**
     * 老师增加班级
     */
    public static final String ADD_CLASS = "/c/rest/a/s/tch/class/add";

    /**
     * 老师删除班级
     */
    public static final String DELETE_CLASS = "/c/rest/a/s/tch/class/delete";

    /**
     * 老师查找班级
     */
    public static final String FIND_CLASS = "/c/rest/a/s/tch/class/find";

    /**
     * 老师修改班级
     */
    public static final String UPDATE_CLASS = "/c/rest/a/s/tch/class/update";

    /**
     * 老师查询班级的所有学生
     */
    public static final String GET_STUDENTS = "/c/rest/a/s/tch/class/findStudents";

    /**
     * 老师删除一个学生
     */
    public static final String DELETE_STUDENT = "/c/rest/a/s/tch/class/deleteStudent";

    /**
     * 老师查询班级作业
     */
    public static final String TEACHER_GET_HOMEWORK_LIST = "/c/rest/a/s/tch/work/find";

    /**
     * 老师获取作业详情(老师专用) getHomeworkDetail
     */
    public static final String TEACHER_GET_HOMEWORK_DETAIL =
            "/c/rest/a/s/tch/work/getHomeworkDetail";

    /**
     * 获取班级下所有存在作业的月份 - getClassAllMonthExistHomework
     */
    public static final String TEACHER_GET_CLASS_ALL_MONTH_EXIST_HOMEWORK =
            "/c/rest/a/s/tch/statistic/getClassAllMonthExistHomework";

    /**
     * 老师获取作业下某个月的所有作业成绩分布 - getClassMonthlyScoreDistribution(老师)
     */
    public static final String TEACHER_GET_CLASS_MONTHLY_SCORE_DISTRIBUTION =
            "/c/rest/a/s/tch/statistic/getClassMonthlyScoreDistribution";

    /**
     * 老师获取班级下所有学生月统计阶段情况 - getClassMonthlyScoreDistributing
     */
    public static final String TEACHER_GET_CLASS_MONTHLY_SCORE_DISTRIBUTING =
            "/c/rest/a/s/tch/work/getClassMonthlyScoreDistributing";

    /**
     * 老师获取班级下一个学生单次成绩分布 - getStudentEveryOnceHomeworkScore
     */
    public static final String TEACHER_GET_STUDENT_EVERYONCE_HOMEWORKSCORE =
            "/c/rest/a/s/tch/statistic/getStudentEveryOnceHomeworkScore";

    /**
     * 老师获取班级下一个学生月成绩分布 - getStudentMonthlyHomeworkScore
     */
    public static final String TEACHER_GET_STUDENT_MONTHLY_HOMEWORKSCORE =
            "/c/rest/a/s/tch/statistic/getStudentMonthlyHomeworkScore";

    /**
     * 老师获取作业下的一个学生作业详情 - getStudentOnHomeworkDetail(老师)
     */
    public static final String TEACHER_GET_STUDENT_ON_HOMEWORK_DETAIL =
            "/c/rest/a/s/tch/work/getStudentOnHomeworkDetail";

    /**
     * 老师获取作业下的所有学生排名情况 - getAllStudentOnHomeworkRanking
     */
    public static final String TEACHER_GET_ALL_STUDENT_ON_HOMEWORK_RANKING =
            "/c/rest/a/s/tch/work/getAllStudentOnHomeworkRanking";

    /**
     * 老师获取作业下的所有学生分布情况 - getAllStudentOnHomeworkDistribution(老师)
     */
    public static final String TEACHER_GET_ALL_STUDENT_ON_HOMEWORK_DISTRIBUTION =
            "/c/rest/a/s/tch/work/getAllStudentOnHomeworkDistribution";

    // 布置作业
    // public static final String HOMEWORK_QUESTION_PRECUT =
    // "/c/rest/a/s/tch/work/prepare";
    public static final String HOMEWORK_ARRANGE = "/c/rest/a/s/tch/work/arrange";

    // 出习题
    public static final String PAPER_PRECUT = "/c/rest/a/s/tch/paper/prepare";
    public static final String PAPER_CREATE = "/c/rest/a/s/tch/paper/save";
    public static final String PAPER_GET = "/c/rest/a/s/tch/paper/list";
    public static final String PAPER_RENAME = "/c/rest/a/s/tch/paper/updateTitle";
    public static final String PAPER_REMOVE = "/c/rest/a/s/tch/paper/remove";
    public static final String PAPER_BASE_GET = "/c/rest/a/s/tch/paper/getQuestionBaseList";
    public static final String PAPER_DETAIL_GET = "/c/rest/a/s/tch/paper/getQuestionDetailList";

    // 改作业
    public static final String HOMEWORK_QUESTION_DETAIL =
            "/c/rest/a/s/tch/work/getQuestionStudentResultList";
    public static final String HOMEWORK_QUESTION_CHECK = "/c/rest/a/s/tch/work/checkQuestion";
    public static final String HOMEWORK_QUESTION_RECOMMANED =
            "/c/rest/a/s/tch/work/recommendQuestion";
    public static final String HOMEWORK_QUESTION_RECOMMANED_CANCEL =
            "/c/rest/a/s/tch/work/cancelRecommendQuestion";
    public static final String HOMEWORK_CHECK_SUBMIT = "/c/rest/a/s/tch/work/checkHomework";
    public static final String HOMEWORK_QUESTIONS_GET =
            "/c/rest/a/s/tch/work/getHomeworkQuestionList";
    public static final String HOMEWORK_DEADLINE_UPDATE = "/c/rest/a/s/tch/work/updateDeadline";

    public static final String HOMEWORK_REMIND = "/c/rest/a/s/tch/work/remindStudentsSubmit";
    public static final String HOMEWORK_SUBMIT_INFO = "/c/rest/a/s/tch/work/getSubmitInfo";
    public static final String HOMEWORK_QUESTION_RESULT_OBJECT =
            "/c/rest/a/s/tch/work/getObjectiveQuestionStudentAnswerList";

    /**
     * 学生根据班级代码查找班级
     */
    public static final String STUDENT_FIND_CLASS = "/c/rest/a/s/stu/class/get";

    /**
     * 学生加入班级
     */
    public static final String STUDENT_JOIN_CLASS = "/c/rest/a/s/stu/class/join";

    /**
     * 学生查询加入的所有班级
     */
    public static final String STUDENT_GET_CLASS = "/c/rest/a/s/stu/class/find";

    /**
     * 学生获取为完成的作业题目
     */
    public static final String STUDENT_GET_QUESTIONSFORSUBMIT =
            "/c/rest/a/s/stu/work/getQuestionsForSubmit";

    /**
     * 学生提交完成的作业
     */
    public static final String STUDENT_SUBMIT_HOMEWORK = "/c/rest/a/s/stu/work/submit";

    /**
     * 学生获取作业详情
     */
    public static final String STUDENT_GET_HOMEWORK_DETAIL =
            "/c/rest/a/s/stu/work/getHomeworkDetail";

    /**
     * 学生获取作业详情
     */
    public static final String STUDENT_GET_HOMEWORK_QUESTION_LIST =
            "/c/rest/a/s/stu/work/getHomeworkQuestionList";

    /**
     * 学生通过resultId获取已批改的作业详情
     */
    public static final String STUDENT_GET_CHECKED_HOMEWORK_DETAIL_BY_RESULTID =
            "/c/rest/a/s/stu/work/getHomeworkQuestionListByResultId";

    /**
     * 学生查询班级已经完成的作业(分页查找)
     */
    public static final String STUDENT_GET_HOMEWORK = "/c/rest/a/s/stu/work/find";

    /**
     * 用户获取消息
     */
    public static final String GET_MESSAGES = "/c/rest/a/s/msg/findMsgs";

    /**
     * 用户删除消息
     */
    public static final String DELETE_MESSAGES = "/c/rest/a/s/msg/removeMsg";

    /**
     * 用户消息标为已读
     */
    public static final String MARKREADED_MESSAGES = "/c/rest/a/s/msg/markRead";

    /**
     * 首页获取数据
     */
    public static final String HOMEPAGE_DATA = "/c/rest/r/index/index";

    /**
     * 最新揭晓获取数据
     */
    public static final String LATEST_PUBLISH = "/c/rest/r/publish/refresh";

    /**
     * 产品详情
     */
    public static final String PRODUCT_PIC_DETAIL = "/c/rest/r/publish/itemDetail";

    /**
     * 图文详情
     */
    public static final String PRODUCT_DETAIL = "/c/rest/r/publish/item";

    /**
     * 中奖码详情
     */
    public static final String CAL_DETAIL = "/c/rest/r/publish/computeDetail";

    /**
     * 查看商品用户参与记录
     */
    public static final String PRODUCT_JOIN_RECORD = "/c/rest/r/publish/partakeHistory";

    /**
     * 查看商品用户参与记录
     */
    public static final String PRODUCT_CLASSIFY = "/c/rest/r/index/category";

    /**
     * 查看用户夺宝记录
     */
    public static final String BUY_RECORD = "/c/rest/r/home/record";

    /**
     * 晒单列表
     */
    public static final String SHARE_ORDER = "/c/rest/r/release/orderList";

    /**
     * 晒单列表
     */
    public static final String SHARE_DETAIL = "/c/rest/r/release/detail";

    /**
     * 购物车刷新
     */
    public static final String SHOPPINGCART = "/c/rest/r/shop/shoppingCart";

    /**
     * 产品分类列表
     */
    public static final String PRODUCT_CLASSIFY_LIST = "/c/rest/r/index/listByType";

    /**
     * 晒单详情
     */
    public static final String SHARE_ORDER_DETAIL = "/c/rest/r/release/detail";

    /**
     * 往期揭晓
     */
    public static final String ANNOUNCE_HISTORY = "/c/rest/r/publish/shopHistory";

    /**
     * 当前用户购买记录
     */
    public static final String MY_BUY_RECORD = "/c/rest/r/publish/history";

    /**
     * 微信登录
     */
    public static final String WEIXIN_LOGIN = "/c/rest/r/user/wxlogin";

    /**
     * 支付列表
     */
    public static final String PAYWAY_LIST = "/c/rest/r/shop/payList";

    /**
     * 微信支付
     */
    public static final String WEIXIN_PAY = "/c/rest/r/home/wx_recharge";

    /**
     * 微信支付
     */
    public static final String IAPP_PAY = "/c/rest/r/home/iapppay_recharge";

    /**
     * 余额支付
     */
    public static final String BALANCE_PAY = "/c/rest/r/shop/pay";

    /**
     * 账户详情
     */
    public static final String SHOWACCOUNT = "/c/rest/r/account/showAccount";

    /**
     * 收货地址
     */
    public static final String ADDRESS_LIST = "/c/rest/r/address/showAddressList";

    /**
     * 添加地址
     */
    public static final String ADD_ADDRESS = "/c/rest/r/address/addAddress";

    /**
     * 修改地址
     */
    public static final String UPDATE_ADDRESS = "/c/rest/r/address/updateAddress";

    /**
     * 删除地址
     */
    public static final String DELETE_ADDRESS = "/c/rest/r/address/deleteAddress";

    /**
     * 更改头像
     */
    public static final String CHANGE_HEAD_IMAGE = "/c/rest/r/userInfo/changeHeadImg";

    /**
     * 我的晒单
     */
    public static final String MY_SHARE = "/c/rest/r/myRelease/orderMyList";

    /**
     * 我的未晒单
     */
    public static final String MY_UN_SHARE = "/c/rest/r/myRelease/showUnrelease";

    /**
     * 添加晒单
     */
    public static final String ADD_SHARE = "/c/rest/r/myRelease/addRelease";

    /**
     * 充值列表
     */
    public static final String RECHARGE_LIST = "/c/rest/r/home/rechargeList";

    /**
     * 修改昵称
     */
    public static final String CHANGE_NICK = "/c/rest/r/userInfo/changeName";

    /**
     * 获得商品
     */
    public static final String OBTAINED_ITEM = "/c/rest/r/obtainItem/obtainedItem";

    /**
     * 未中商品
     */
    public static final String NOT_OBTAINED_ITEM = "/c/rest/r/obtainItem/notObtainedItem";

    /**
     * 刷新用户信息
     */
    public static final String REFRESH_USER_INFO = "/c/rest/r/home/refresh";

    /**
     * 修改密码
     */
    public static final String MODIFY_PASSWD = "/c/rest/r/userInfo/changePwd";

}
