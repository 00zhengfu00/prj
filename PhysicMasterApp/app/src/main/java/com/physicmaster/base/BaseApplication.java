package com.physicmaster.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.sdk.android.feedback.impl.FeedbackAPI;
import com.alibaba.sdk.android.feedback.util.ErrorCode;
import com.alibaba.sdk.android.feedback.util.FeedbackErrorCallback;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.contact.core.query.PinYin;
import com.netease.nim.uikit.custom.DefalutUserInfoProvider;
import com.netease.nim.uikit.session.viewholder.MsgViewHolderThumbBase;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.ServerAddresses;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.model.AVChatAttachment;
import com.netease.nimlib.sdk.mixpush.NIMPushClient;
import com.netease.nimlib.sdk.msg.MessageNotifierCustomization;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum;
import com.netease.nimlib.sdk.team.model.IMMessageFilter;
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment;
import com.physicmaster.R;
import com.physicmaster.common.Constant;
import com.physicmaster.common.DeviceUuidFactory;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.modules.discuss.DemoCache;
import com.physicmaster.modules.discuss.PrivatizationConfig;
import com.physicmaster.modules.discuss.config.ExtraOptions;
import com.physicmaster.modules.discuss.config.preference.Preferences;
import com.physicmaster.modules.discuss.config.preference.UserPreferences;
import com.physicmaster.modules.discuss.contact.ContactHelper;
import com.physicmaster.modules.discuss.event.DemoOnlineStateContentProvider;
import com.physicmaster.modules.discuss.event.OnlineStateEventManager;
import com.physicmaster.modules.discuss.session.SessionHelper;
import com.physicmaster.net.response.account.StartupResponse;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.utils.SystemUtil;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.umeng.socialize.PlatformConfig;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Created by huazai on 22/04/16.
 */
public class BaseApplication extends DefaultApplicationLike {
    private static Context context;
    private static int mScreenWidth;
    private static float density;
    private static String IMEI;
    private static String IMSI;
    private static int mScreenHeight;
    private static String deviceID;
    private static UserDataResponse.UserDataBean.KeyVoBean userKey;
    private static UserDataResponse.UserDataBean.LoginVoBean loginVoBean;
    private static StartupResponse.DataBean startupDataBean;
    private static BaseApplication instance;
    private static String packageName;
    private static String version;
    private static int none_wifi_prompt_times = 0;
    //应用启动从服务器拿到的时间
    private static long serverTime = 0;
    //应用启动拿到的客户端本地开机时间
    private static long startTime = 0;

    public static final String TAG = "com.physicmaster";
//    private static RefWatcher refWatcher;

    private static boolean isNimLogin = false;

    @Override
    public void onCreate() {
        context = getApplication();
        super.onCreate();
        initDeviceData();
        //bugly 初始化
        String packageName = getApplication().getPackageName();
        BaseApplication.packageName = packageName;
        if (packageName.equals(Constant.PHYSICMASTER)) {
            Bugly.init(getApplication(), Constant.PM_BUGLY_APPKEY, true);
        } else if (packageName.equals(Constant.CHYMISTMASTER)) {
            Bugly.init(getApplication(), Constant.CM_BUGLY_APPKEY, true);
        } else if (packageName.equals(Constant.MATHMASTER)) {
            Bugly.init(getApplication(), Constant.MM_BUGLY_APPKEY, true);
        }
        //友盟初始化
        umengInit(packageName);
        xiaomiInit(packageName);
        //阿里百川反馈模块初始化
        if (packageName.equals(Constant.PHYSICMASTER)) {
            FeedbackAPI.init(getApplication(), Constant.PM_ALIBAICHUAN_APPKEY);
        } else if (packageName.equals(Constant.CHYMISTMASTER)) {
            FeedbackAPI.init(getApplication(), Constant.CM_ALIBAICHUAN_APPKEY);
        } else if (packageName.equals(Constant.MATHMASTER)) {
            FeedbackAPI.init(getApplication(), Constant.MM_ALIBAICHUAN_APPKEY);
        }
        // 自定义ErrorCallback
        FeedbackAPI.addErrorCallback(new FeedbackErrorCallback() {
            @Override
            public void onError(Context context, String errorMessage, ErrorCode code) {
                Toast.makeText(context, "ErrMsg is: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        // Feedback activity的回调
        FeedbackAPI.addLeaveCallback(new Callable() {
            @Override
            public Object call() throws Exception {
                Log.d("DemoApplication", "custom leave callback");
                return null;
            }
        });

        //内存泄露
//        if (!LeakCanary.isInAnalyzerProcess(getApplication())) {
        // This process is dedicated to LeakCanary for heap analysis.
        // You should not initData your app in this process.
//            refWatcher = LeakCanary.install(getApplication());
//        }

        //网易云信
        DemoCache.setContext(getApplication());
        // 注册小米推送appID 、appKey 以及在云信管理后台添加的小米推送证书名称，该逻辑放在 NIMClient initData 之前
        nimXiaomiInit(getPackageName());
        // 注册自定义小米推送消息处理，这个是可选项
//        NIMPushClient.registerMixPushMessageHandler(new DemoMixPushMessageHandler());
        NIMClient.init(getApplication(), getLoginInfo(), getOptions());
        ExtraOptions.provide();
        if (inMainProcess()) {

            // initData pinyin
            PinYin.init(getApplication());
            PinYin.validate();

            // 初始化UIKit模块
            initUIKit();

            // 注册通知消息过滤器
            registerIMMessageFilter();

            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

            // 注册网络通话来电
//            registerAVChatIncomingCallObserver(true);

            // 注册白板会话
//            registerRTSIncomingObserver(true);

            // 注册语言变化监听
//            registerLocaleReceiver(true);

            OnlineStateEventManager.init();
        }
    }

    public boolean inMainProcess() {
        String packageName = getPackageName();
        String processName = SystemUtil.getProcessName(getApplication());
        return packageName.equals(processName);
    }

    private void initUIKit() {
        // 初始化，使用 uikit 默认的用户信息提供者
        NimUIKit.init(getApplication());

        // 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
//        NimUIKit.setLocationProvider(new NimDemoLocationProvider());

        // 会话窗口的定制初始化。
        SessionHelper.init();

        // 通讯录列表定制初始化
        ContactHelper.init();

        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
        // NimUIKit.CustomPushContentProvider(new DemoPushContentProvider());

        NimUIKit.setOnlineStateContentProvider(new DemoOnlineStateContentProvider());
    }

    private LoginInfo getLoginInfo() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    private SDKOptions getOptions() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
        initStatusBarNotificationConfig(options);

        // 配置保存图片，文件，log等数据的目录
        options.sdkStorageRootPath = Environment.getExternalStorageDirectory() + "/" +
                getPackageName() + "/nim";

        // 配置数据库加密秘钥
        options.databaseEncryptKey = "NETEASE";

        // 配置是否需要预下载附件缩略图
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小，
        options.thumbnailSize = MsgViewHolderThumbBase.getImageMaxEdge();

        // 用户信息提供者
        options.userInfoProvider = new DefalutUserInfoProvider(getApplication());

        // 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
        options.messageNotifierCustomization = messageNotifierCustomization;

        // 在线多端同步未读数
        options.sessionReadAck = true;

        // 云信私有化配置项
        configServerAddress(options);

        return options;
    }

    private void initStatusBarNotificationConfig(SDKOptions options) {
        // load 应用的状态栏配置
        StatusBarNotificationConfig config = loadStatusBarNotificationConfig();

        // load 用户的 StatusBarNotificationConfig 设置项
        StatusBarNotificationConfig userConfig = UserPreferences.getStatusConfig();
        if (userConfig == null) {
            userConfig = config;
        } else {
            // 新增的 UserPreferences 存储项更新，兼容 3.4 及以前版本
            // 新增 notificationColor 存储，兼容3.6以前版本
            // APP默认 StatusBarNotificationConfig 配置修改后，使其生效
            userConfig.notificationEntrance = config.notificationEntrance;
            userConfig.notificationFolded = config.notificationFolded;
            userConfig.notificationColor = getApplication().getResources().getColor(R.color.color_blue_3a9efb);
        }
        // 持久化生效
        UserPreferences.setStatusConfig(userConfig);
        // SDK statusBarNotificationConfig 生效
        options.statusBarNotificationConfig = userConfig;
    }

    // 这里开发者可以自定义该应用初始的 StatusBarNotificationConfig
    private StatusBarNotificationConfig loadStatusBarNotificationConfig() {
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        // 点击通知需要跳转到的界面
        config.notificationEntrance = MainActivity.class;
        config.notificationSmallIconId = R.drawable.ic_stat_notify_msg;
        config.notificationColor = getApplication().getResources().getColor(R.color.color_blue_3a9efb);
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://" + getPackageName() + "/raw/msg";

        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;

        // save cache，留做切换账号备用
        DemoCache.setNotificationConfig(config);
        return config;
    }

    /**
     * 通知消息过滤器（如果过滤则该消息不存储不上报）
     */
    private void registerIMMessageFilter() {
        NIMClient.getService(MsgService.class).registerIMMessageFilter(new IMMessageFilter() {
            @Override
            public boolean shouldIgnore(IMMessage message) {
                if (UserPreferences.getMsgIgnore() && message.getAttachment() != null) {
                    if (message.getAttachment() instanceof UpdateTeamAttachment) {
                        UpdateTeamAttachment attachment = (UpdateTeamAttachment) message
                                .getAttachment();
                        for (Map.Entry<TeamFieldEnum, Object> field : attachment.getUpdatedFields
                                ().entrySet()) {
                            if (field.getKey() == TeamFieldEnum.ICON) {
                                return true;
                            }
                        }
                    } else if (message.getAttachment() instanceof AVChatAttachment) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private MessageNotifierCustomization messageNotifierCustomization = new
            MessageNotifierCustomization() {
                @Override
                public String makeNotifyContent(String nick, IMMessage message) {
                    return null; // 采用SDK默认文案
                }

                @Override
                public String makeTicker(String nick, IMMessage message) {
                    return null; // 采用SDK默认文案
                }
            };

    private void configServerAddress(final SDKOptions options) {
        String appKey = PrivatizationConfig.getAppKey();
        if (!TextUtils.isEmpty(appKey)) {
            options.appKey = appKey;
        }

        ServerAddresses serverConfig = PrivatizationConfig.getServerAddresses();
        if (serverConfig != null) {
            options.serverConfig = serverConfig;
        }
    }

    private void umengInit(String packageName) {
        if (packageName.equals(Constant.PHYSICMASTER)) {
            PlatformConfig.setWeixin(Constant.PM_WEIXIN_APP_ID, Constant.PM_WEIXIN_APP_SECRET);
            PlatformConfig.setQQZone(Constant.PM_QQ_APP_ID, Constant.PM_QQ_APP_KEY);
            PlatformConfig.setSinaWeibo(Constant.PM_WEIBO_APP_KEY, Constant.PM_WEIBO_APP_SECRET,
                    Constant.WEIBO_REDIRECT_URL);
        } else if (packageName.equals(Constant.CHYMISTMASTER)) {
            PlatformConfig.setWeixin(Constant.CM_WEIXIN_APP_ID, Constant.CM_WEIXIN_APP_SECRET);
            PlatformConfig.setQQZone(Constant.CM_QQ_APP_ID, Constant.CM_QQ_APP_KEY);
            PlatformConfig.setSinaWeibo(Constant.CM_WEIBO_APP_KEY, Constant.CM_WEIBO_APP_SECRET,
                    Constant.WEIBO_REDIRECT_URL);
        } else if (packageName.equals(Constant.MATHMASTER)) {
            PlatformConfig.setWeixin(Constant.MM_WEIXIN_APP_ID, Constant.MM_WEIXIN_APP_SECRET);
            PlatformConfig.setQQZone(Constant.MM_QQ_APP_ID, Constant.MM_QQ_APP_KEY);
            PlatformConfig.setSinaWeibo(Constant.MM_WEIBO_APP_KEY, Constant.MM_WEIBO_APP_SECRET,
                    Constant.WEIBO_REDIRECT_URL);
        }
    }

    /**
     * 云信小米推送
     *
     * @param packageName
     */
    private void nimXiaomiInit(String packageName) {//pmXiaoMi cmXiaoMi mmXiaoMi
        if (packageName.equals(Constant.PHYSICMASTER)) {
            NIMPushClient.registerMiPush(getApplication(), "pmXiaoMi",
                    Constant.PM_XIAOMI_APP_ID, Constant.PM_XIAOMI_APP_KEY);
        } else if (packageName.equals(Constant.CHYMISTMASTER)) {
            NIMPushClient.registerMiPush(getApplication(), "cmXiaoMi",
                    Constant.CM_XIAOMI_APP_ID, Constant.CM_XIAOMI_APP_KEY);
        } else if (packageName.equals(Constant.MATHMASTER)) {
            NIMPushClient.registerMiPush(getApplication(), "mmXiaoMi",
                    Constant.MM_XIAOMI_APP_ID, Constant.MM_XIAOMI_APP_KEY);
        }
    }

//    public static RefWatcher getRefWatcher(Context context) {
//        return BaseApplication.refWatcher;
//    }

    public static Context getAppContext() {
        return context;
    }

    private void initDeviceData() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplication().getSystemService(Context
                .WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        SharedPreferences preferences = getApplication().getSharedPreferences(Constant
                .ACCOUNT_PF, getApplication().MODE_PRIVATE);
        String uuid = "";
        if (TextUtils.isEmpty(preferences.getString("deviceID", ""))) {
            SharedPreferences.Editor edit = preferences.edit();
            uuid = new DeviceUuidFactory(getApplication()).getDeviceUuid().toString();
            String packageName = getApplication().getPackageName();
            //如果不是物理大师app,需要加上包名
            if (!Constant.PHYSICMASTER.equals(packageName)) {
                uuid = UUID.nameUUIDFromBytes((packageName + uuid).getBytes()).toString();
            }
            edit.putString("deviceID", uuid);
            edit.putString("widthPixels", metrics.widthPixels + "");
            edit.putString("heightPixels", metrics.heightPixels + "");
            edit.putString("density", metrics.density + "");
            edit.putString("deviceName", Build.MODEL);
            edit.putBoolean("first", false);
            edit.commit();
        } else {
            uuid = preferences.getString("deviceID", "");
        }
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        density = metrics.density;
        deviceID = uuid;
        TelephonyManager telephonyManager = (TelephonyManager) getAppContext().getSystemService
                (Context.TELEPHONY_SERVICE);
        IMEI = telephonyManager.getDeviceId();
        IMSI = telephonyManager.getSubscriberId();
        PackageManager manager = getApplication().getPackageManager();
        try {
            version = manager.getPackageInfo(getApplication().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            version = "2.0.0";
            e.printStackTrace();
        }
        UserDataResponse.UserDataBean.KeyVoBean keyVoBean = (UserDataResponse.UserDataBean
                .KeyVoBean) CacheManager.getObject(CacheManager.TYPE_USER_INFO, CacheKeys
                .USERINFO_USERKEY, UserDataResponse.UserDataBean.KeyVoBean.class);
        if (keyVoBean != null) {
            userKey = keyVoBean;
        }
        UserDataResponse.UserDataBean.LoginVoBean userDataBean = (UserDataResponse.UserDataBean
                .LoginVoBean) CacheManager.getObject(CacheManager.TYPE_USER_INFO, CacheKeys
                .USERINFO_LOGINVO, UserDataResponse.UserDataBean.LoginVoBean.class);
        if (userDataBean != null) {
            loginVoBean = userDataBean;
        }

        //设置bugly渠道
        CrashReport.setAppChannel(getApplication(), getChannel());
    }

    public static int getScreenWidth() {
        return mScreenWidth;
    }

    public static int getScreenHeight() {
        return mScreenHeight;
    }

    public static String getDeviceID() {
        return deviceID;
    }

    public static float getDensity() {
        return density;
    }

    public static String getIMEI() {
        return IMEI;
    }

    public static String getIMSI() {
        return IMSI;
    }

    public static UserDataResponse.UserDataBean.KeyVoBean getUserKey() {
        return userKey;
    }

    public static void setUserKey(UserDataResponse.UserDataBean.KeyVoBean userKey) {
        BaseApplication.userKey = userKey;
    }

    public static UserDataResponse.UserDataBean.LoginVoBean getUserData() {
        return loginVoBean;
    }

    public static void setUserData(UserDataResponse.UserDataBean.LoginVoBean loginDataBean) {
        loginVoBean = loginDataBean;
    }

    public static StartupResponse.DataBean getStartupDataBean() {
        return startupDataBean;
    }

    public static void setStartupDataBean(StartupResponse.DataBean startupDataBean) {
        BaseApplication.startupDataBean = startupDataBean;
    }

    public static String getVersion() {
        return version;
    }

    private void xiaomiInit(String packageName) {
        //初始化push推送服务
        if (shouldInit()) {
            if (packageName.equals(Constant.PHYSICMASTER)) {
                MiPushClient.registerPush(getApplication(), Constant.PM_XIAOMI_APP_ID, Constant
                        .PM_XIAOMI_APP_KEY);
            } else if (packageName.equals(Constant.CHYMISTMASTER)) {
                MiPushClient.registerPush(getApplication(), Constant.CM_XIAOMI_APP_ID, Constant
                        .CM_XIAOMI_APP_KEY);
            } else if (packageName.equals(Constant.MATHMASTER)) {
                MiPushClient.registerPush(getApplication(), Constant.MM_XIAOMI_APP_ID, Constant
                        .MM_XIAOMI_APP_KEY);
            }
        }
        //打开Log
        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(getApplication(), newLogger);
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getApplication().getSystemService(Context
                .ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        if (null == processInfos) {
            return false;
        }
        String mainProcessName = getApplication().getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    private String getChannel() {
        String channel = "Android";
        ApplicationInfo info = null;
        try {
            info = getApplication().getPackageManager().getApplicationInfo(getApplication()
                    .getPackageName(), PackageManager
                    .GET_META_DATA);
            if (info.metaData == null) {
                return channel;
            }
            channel = info.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channel;
    }

    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);

        // 安装tinker
        // TinkerManager.installTinker(this); 替换成下面Bugly提供的方法
        Beta.installTinker(this);
    }

    public BaseApplication(Application application, int tinkerFlags,
                           boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime,
                           long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime,
                applicationStartMillisTime, tinkerResultIntent);
    }

    public void registerActivityLifecycleCallback(Application.ActivityLifecycleCallbacks
                                                          callbacks) {
        getApplication().registerActivityLifecycleCallbacks(callbacks);
    }

    public static String getPackageName() {
        return packageName;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

//    // initData FileDownloader
//    private void initFileDownloader() {
//
//        // 1.create FileDownloadConfiguration.Builder
//        FileDownloadConfiguration.Builder builder = new FileDownloadConfiguration.Builder
//                (getApplication());
//
//        // 2.config FileDownloadConfiguration.Builder
//        builder.configFileDownloadDir(Environment.getExternalStorageDirectory().getAbsolutePath()
//                + File.separator + "physicmaster"); // config the download path
//        // builder.configFileDownloadDir("/storage/sdcard1/FileDownloader");
//
//        // allow 3 download tasks at the same time
//        builder.configDownloadTaskSize(3);
//
//        // config retry download times when failed
//        builder.configRetryDownloadTimes(5);
//
//        // enable debug mode
//        //builder.configDebugMode(true);
//
//        // config connect timeout
//        builder.configConnectTimeout(25000); // 25s
//
//        // 3.initData FileDownloader with the configuration
//        FileDownloadConfiguration configuration = builder.build(); // build
//        // FileDownloadConfiguration with the builder
//        FileDownloader.init(configuration);
//    }
//
//    // release FileDownloader
//    private void releaseFileDownloader() {
//        FileDownloader.release();
//    }

    public static int getNone_wifi_prompt_times() {
        return none_wifi_prompt_times;
    }

    public static void setNone_wifi_prompt_times(int none_wifi_prompt_times) {
        BaseApplication.none_wifi_prompt_times = none_wifi_prompt_times;
    }

    public static long getServerTime() {
        return serverTime;
    }

    public static void setServerTime(long serverTime) {
        BaseApplication.serverTime = serverTime;
    }

    public static long getStartTime() {
        return startTime;
    }

    public static void setStartTime(long startTime) {
        BaseApplication.startTime = startTime;
    }

    public static boolean isNimLogin() {
        return isNimLogin;
    }

    public static void setNimLogin(boolean nimLogin) {
        isNimLogin = nimLogin;
    }
}
