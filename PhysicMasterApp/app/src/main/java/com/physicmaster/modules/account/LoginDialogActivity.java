package com.physicmaster.modules.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.physicmaster.R;
import com.physicmaster.base.BaseActivity;
import com.physicmaster.base.BaseApplication;
import com.physicmaster.base.MainActivity;
import com.physicmaster.base.PlayGuideMusicService;
import com.physicmaster.base.SplashActivity;
import com.physicmaster.common.Constant;
import com.physicmaster.common.cache.CacheKeys;
import com.physicmaster.common.cache.CacheManager;
import com.physicmaster.log.AndroidLogger;
import com.physicmaster.log.Logger;
import com.physicmaster.modules.account.basics.SelectPetActivity;
import com.physicmaster.modules.discuss.DemoCache;
import com.physicmaster.modules.discuss.config.preference.UserPreferences;
import com.physicmaster.modules.ranking.fragment.RankingFragment;
import com.physicmaster.modules.study.fragment.StudyFragmentV2;
import com.physicmaster.net.IOpenApiDataServiceCallback;
import com.physicmaster.net.bean.Login4PhoneBean;
import com.physicmaster.net.response.account.LoginByOpenResponse;
import com.physicmaster.net.response.account.StartupResponse;
import com.physicmaster.net.response.user.UserDataResponse;
import com.physicmaster.net.service.account.Login4PhoneService;
import com.physicmaster.net.service.account.LoginByOpenService;
import com.physicmaster.net.service.account.TouristLoginService;
import com.physicmaster.utils.MD5;
import com.physicmaster.utils.SpUtils;
import com.physicmaster.utils.UIUtils;
import com.physicmaster.utils.Utils;
import com.physicmaster.widget.CustomVideoView;
import com.physicmaster.widget.ProgressLoadingDialog;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.SocializeUtils;

import java.util.Map;

import static com.alibaba.sdk.android.feedback.impl.FeedbackAPI.mContext;

public class LoginDialogActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivEye;
    private EditText etPassword;
    private EditText etUserName;
    private int pwdEye = 0;//默认不显示密码
    public final static String EXTRA_TYPE_USERCLICK = "EXTRA_TYPE_USERCLICK";
    private ImageView ivQQ;
    private ImageView ivWeixin;
    private ImageView ivWeibo;
    private ImageView ivClose;
    private TextView tvRegister;
    private TextView tvForgetPwd;
    private TextView tvTouristLogin;
    private Button btnLogin;
    private long lastLoginInvokeTime = 0;
    private String packageName;
    private ImageView ivMaster;
    private ProgressDialog dialog;
    private ScrollView svLogin;
    private AbortableFuture<LoginInfo> loginRequest;
    public static final String KICK_OUT = "KICK_OUT";
    private static final String TAG = "LoginActivity";
    private Logger logger = AndroidLogger.getLogger(TAG);

    private CustomVideoView mVideoView;

    @Override
    protected void findViewById() {
        etUserName = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);

        ivMaster = (ImageView) findViewById(R.id.iv_master);
        ivEye = (ImageView) findViewById(R.id.iv_eye);
        ivQQ = (ImageView) findViewById(R.id.iv_qq);
        ivWeixin = (ImageView) findViewById(R.id.iv_weixin);
        ivWeibo = (ImageView) findViewById(R.id.iv_weibo);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        tvForgetPwd = (TextView) findViewById(R.id.tv_forget_pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);
        svLogin = (ScrollView) findViewById(R.id.sv_login);
        tvTouristLogin = (TextView) findViewById(R.id.tv_tourist_login);

        mVideoView = (CustomVideoView) findViewById(R.id.videoView);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (TextUtils.isEmpty(etUserName.getText()) || TextUtils.isEmpty(etPassword.getText()) || etPassword.getText().length() < 6) {
                btnLogin.setEnabled(false);
            } else {
                btnLogin.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void initView() {
        ivEye.setOnClickListener(this);
        ivQQ.setOnClickListener(this);
        ivWeixin.setOnClickListener(this);
        ivWeibo.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvForgetPwd.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvTouristLogin.setOnClickListener(this);

        packageName = mContext.getPackageName();
        if (Constant.PHYSICMASTER.equals(packageName)) {
            ivMaster.setImageResource(R.mipmap.login_master);
        } else if (Constant.CHYMISTMASTER.equals(packageName)) {
            ivMaster.setImageResource(R.mipmap.login_chemistry);
        } else if (Constant.MATHMASTER.equals(packageName)) {
            ivMaster.setImageResource(R.mipmap.login_math);
        }

        etUserName.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getBooleanExtra(KICK_OUT, false)) {
                int type = NIMClient.getService(AuthService.class).getKickedClientType();
                String client;
                switch (type) {
                    case ClientType.Web:
                        client = "网页端";
                        break;
                    case ClientType.Windows:
                        client = "电脑端";
                        break;
                    case ClientType.REST:
                        client = "服务端";
                        break;
                    default:
                        client = "移动端";
                        break;
                }
                EasyAlertDialogHelper.showOneButtonDiolag(LoginDialogActivity.this, getString(R.string
                                .kickout_notify),
                        String.format(getString(R.string.kickout_content), client), getString(R
                                .string.ok), true, null);
            }
        }

        mVideoView = (CustomVideoView) findViewById(R.id.videoView);
        //设置播放加载路径
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.login));
        //播放
        mVideoView.start();
        //循环播放
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVideoView.start();
            }
        });
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mVideoView.setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        });
        //清除首页残留数据(如果首页还未销毁的话)
        LocalBroadcastManager.getInstance(LoginDialogActivity.this).sendBroadcast(new Intent(StudyFragmentV2.ON_SUBJECT_CHANGED));
    }


    @Override
    protected void onPause() {
        super.onPause();
        mVideoView.pause();
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(PlayGuideMusicService.FILTER_STOP_SERVICE));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mVideoView.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_to_bottom);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_login_dialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                doLoigin();
                break;
            case R.id.iv_eye:
                pwdEye = pwdEye == 0 ? 1 : 0;
                setIvInfoEye();
                break;
            case R.id.iv_qq:
                if (Constant.PHYSICMASTER.equals(packageName)) {
                    login2QQ();
                } else if (Constant.CHYMISTMASTER.equals(packageName)) {
                    login2QQNOWL();
                } else if (Constant.MATHMASTER.equals(packageName)) {
                    login2QQNOWL();
                }
                break;
            case R.id.iv_weixin:
                login2Weixin();
                break;
            case R.id.iv_weibo:
                login2Weibo();
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.tv_forget_pwd:
                startActivity(new Intent(this, FindPwdActivity.class));
                break;
            case R.id.tv_tourist_login:
                touristLogin();
                break;
            case R.id.iv_close:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_out_to_bottom);
                break;
        }
    }

    private void login2QQNOWL() {
        StartupResponse.DataBean dataBean = BaseApplication.getStartupDataBean();
        if (null != dataBean) {
            String webQqGetCodeUrl = dataBean.webQqGetCodeUrl;
            if (!TextUtils.isEmpty(webQqGetCodeUrl)) {
                Intent intent = new Intent(LoginDialogActivity.this, LoginWebActivity.class);
                intent.putExtra("webQqGetCodeUrl", webQqGetCodeUrl);
                startActivity(intent);
            }
        } else {
            startActivity(new Intent(LoginDialogActivity.this, SplashActivity.class));
            UIUtils.showToast(LoginDialogActivity.this, "数据异常");
            finish();
        }
    }

    private void login2Weixin() {
        if (Utils.checkApkExist(this, "com.tencent.mm")) {
            UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.WEIXIN, authListener);
        } else {
            Toast.makeText(this, "您还未安装微信，请先安装~", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * QQ登录
     */
    private void login2QQ() {
        if (Utils.checkApkExist(this, "com.tencent.mobileqq")) {
            UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.QQ, authListener);
        } else {
            Toast.makeText(this, "您还未安装QQ，请先安装~", Toast.LENGTH_SHORT).show();
        }
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            dialog = new ProgressDialog(LoginDialogActivity.this);
            dialog.setTitle("正在登录");
            dialog.setMessage("请等候……");
            dialog.setCancelable(true);
            SocializeUtils.safeShowDialog(dialog);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            String openId;
            String token = data.get("access_token");
            if (platform.equals(SHARE_MEDIA.SINA)) {
                openId = data.get("uid");
            } else {
                openId = data.get("openid");
            }
            int type = -1;
            if (platform.equals(SHARE_MEDIA.WEIXIN)) {
                type = 0;
            } else if (platform.equals(SHARE_MEDIA.QQ)) {
                type = 1;
            } else if (platform.equals(SHARE_MEDIA.SINA)) {
                type = 2;
            }
            openLogin(openId, type, token);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(mContext, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            SocializeUtils.safeCloseDialog(dialog);
            Toast.makeText(mContext, "取消了", Toast.LENGTH_LONG).show();
        }
    };

    /**
     * 登录到微博
     */
    private void login2Weibo() {
        if (Utils.checkApkExist(this, "com.sina.weibo") || Utils.checkApkExist(this, "com.sina" + ".weibog3")) {
            UMShareAPI.get(this).doOauthVerify(this, SHARE_MEDIA.SINA, authListener);
        } else {
            Toast.makeText(this, "您还未安装微博，请先安装~", Toast.LENGTH_SHORT).show();
        }
    }

    private void doLoigin() {
        String phoneNum = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(password)) {
            UIUtils.showToast(LoginDialogActivity.this, "请输入用户名或密码");
            return;
        }
        if (password.length() < 6) {
            UIUtils.showToast(this, "手机或密码错误");
            return;
        }

        String miPushId = SpUtils.getString(this, SpUtils.MI_PUSH_ID, "");
        Login4PhoneBean bean = new Login4PhoneBean(phoneNum, MD5.hexdigest(password), miPushId);
        final Login4PhoneService service = new Login4PhoneService(this);
        final ProgressLoadingDialog loadingDialog = new ProgressLoadingDialog(this);
        service.setCallback(new IOpenApiDataServiceCallback<UserDataResponse>() {
            @Override
            public void onGetData(UserDataResponse data) {
                logger.debug("登录成功" + data.toString());
                loginAfterAction(data.data);
                loadingDialog.dismissDialog();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                logger.debug("登录失败" + errorMsg);
                UIUtils.showToast(LoginDialogActivity.this, errorMsg);
                loadingDialog.dismissDialog();
            }
        });
        loadingDialog.showDialog(new ProgressLoadingDialog.onCancelListener() {
            @Override
            public void onCancel() {
                service.cancel();
            }
        });
        service.postUnLogin(bean.toString(), false);
    }

    /**
     * 登录成功操作
     *
     * @param data
     */
    private void loginAfterAction(UserDataResponse.UserDataBean data) {
        data.loginVo.isLoginByPhone = true;
        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                .USERINFO_USERKEY, data.keyVo);
        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                .USERINFO_LOGINVO, data.loginVo);
        BaseApplication.setUserData(data.loginVo);
        BaseApplication.setUserKey(data.keyVo);
        if (data.loginVo.isInitial == 0) {
            Intent intent = new Intent(LoginDialogActivity.this, SelectPetActivity.class);
            UIUtils.showToast(LoginDialogActivity.this, "请完善基本信息");
            startActivity(intent);
        } else {
            LocalBroadcastManager.getInstance(LoginDialogActivity.this).sendBroadcast(new Intent(RankingFragment.SWITCH_ACCOUNT));
            Intent intent = new Intent(LoginDialogActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            hideInputSoft(this, etPassword);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    /**
     * ***************************************** 云信登录 **************************************
     */

    private void login() {
        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 这里为了简便起见，demo就直接使用了密码的md5作为token。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。
        final String account = "gentu_test_1";
        final String token = "111111";
        // 登录
        loginRequest = NimUIKit.doLogin(new LoginInfo(account, token), new
                RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        logger.info("login success");
                        // 初始化消息提醒配置
                        initNotificationConfig();
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == 302 || code == 404) {
                            Toast.makeText(LoginDialogActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginDialogActivity.this, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        Toast.makeText(LoginDialogActivity.this, R.string.login_exception, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }

    private void setIvInfoEye() {
        switch (pwdEye) {
            case 0:
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                //显示密文密码
                ivEye.setBackground(getResources().getDrawable(R.mipmap.login_close));
                //ivEye.setVisibility(View.VISIBLE);
                break;
            case 1:
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
                ;//显示明文密码
                ivEye.setBackground(getResources().getDrawable(R.mipmap.login_open));
                // ivEye.setVisibility(View.VISIBLE);
                break;
        }
        etPassword.setSelection(etPassword.getText().length());
    }

    /**
     * 第三方登录
     */
    private void openLogin(String openId, int type, String token) {
        //解决莫名其妙的重复调用问题
        long curLoginInvokeTime = System.currentTimeMillis();
        if (lastLoginInvokeTime != 0 && (curLoginInvokeTime - lastLoginInvokeTime < 1000)) {
            lastLoginInvokeTime = curLoginInvokeTime;
            return;
        }
        lastLoginInvokeTime = curLoginInvokeTime;
        final LoginByOpenService service = new LoginByOpenService(this);
        service.setCallback(new IOpenApiDataServiceCallback<LoginByOpenResponse>() {
            @Override
            public void onGetData(LoginByOpenResponse data) {
                loginAfterAction(data.data);
                SocializeUtils.safeCloseDialog(dialog);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(LoginDialogActivity.this, errorMsg);
            }
        });
        StringBuilder builder = new StringBuilder();
        builder.append("openid=" + openId);
        builder.append("&btype=" + type);
        builder.append("&token=" + token);
        String miPushId = SpUtils.getString(this, SpUtils.MI_PUSH_ID, "");
        if (!TextUtils.isEmpty(miPushId)) {
            builder.append("&miPush=" + miPushId);
        }
        service.postUnLogin(builder.toString(), false);
    }

    /**
     * 游客登录
     */
    private void touristLogin() {
        dialog = new ProgressDialog(LoginDialogActivity.this);
        dialog.setTitle("正在登录");
        dialog.setMessage("请等候……");
        dialog.setCancelable(true);
        SocializeUtils.safeShowDialog(dialog);
        TouristLoginService service = new TouristLoginService(this);
        service.setCallback(new IOpenApiDataServiceCallback<UserDataResponse>() {
            @Override
            public void onGetData(UserDataResponse data) {
                loginAfterAction(data.data);
                SocializeUtils.safeCloseDialog(dialog);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(LoginDialogActivity.this, errorMsg);
                SocializeUtils.safeCloseDialog(dialog);
            }
        });
        service.postUnLogin("", false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
        hideInputSoft(this, etPassword);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }
}
