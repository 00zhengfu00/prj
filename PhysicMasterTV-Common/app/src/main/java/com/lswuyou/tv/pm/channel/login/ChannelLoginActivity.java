package com.lswuyou.tv.pm.channel.login;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Toast;

import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.activity.BaseActivity;
import com.lswuyou.tv.pm.activity.CoursePackDetailActivity;
import com.lswuyou.tv.pm.channel.pay.TvChannelType;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.fragment.UserFragment;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.response.CommonResponse;
import com.lswuyou.tv.pm.net.response.account.LoginByChannelResponse;
import com.lswuyou.tv.pm.net.response.account.LoginUserInfo;
import com.lswuyou.tv.pm.net.response.account.RegisterByQdResponse;
import com.lswuyou.tv.pm.net.service.BindQdService;
import com.lswuyou.tv.pm.net.service.LoginByChannelService;
import com.lswuyou.tv.pm.net.service.RegisterByQdService;
import com.lswuyou.tv.pm.view.BindWeixinDialog;
import com.lswuyou.tv.pm.view.TitleBarView;

public class ChannelLoginActivity extends BaseActivity {
    private TitleBarView mTitleBarView;
    private BindWeixinDialog bindWeixinDialog;
//    private AliPlayLoginImpl aliPlayLoginImpl;
    // READ_PHONE_STATE权限请求码
    private final static int READ_PHONE_STATE_REQUEST_CODE = 1;
    /**
     * Android6.0需要申请权限
     */
    private final static String DANGEROUS_PERMISSION[] = new String[]{Manifest.permission
            .READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView) findViewById(R.id.title);
    }

    @Override
    protected void initView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
        mTitleBarView.setBtnLeft(0, R.string.login);
        String channel = getIntent().getStringExtra("channel");
        if (channel.equals(TvChannelType.xiaomi.name())) {
//            MiCommplatform.getInstance().miLogin(mContext,
//                    new OnLoginProcessListener() {
//                        @Override
//                        public void finishLoginProcess(int i, MiAccountInfo
//                                miAccountInfo) {
//                            loginByChannel(miAccountInfo.getNikename(), "", miAccountInfo
//                                    .getUid() + "");
//                        }
//                    });
            loginByChannel("huashigen", "", "1929733773");
        } else if (channel.equals(TvChannelType.leshi.name())) {
//            LeshiLoginImpl leshiLogin = new LeshiLoginImpl(this);
//            leshiLogin.setLoginCallBackListener(new LoginCallBackListener() {
//                @Override
//                public void onLogout() {
//                    Toast.makeText(getApplicationContext(), "注销成功", Toast.LENGTH_SHORT).show();
//                    Log.e("result", "注销成功");
//                    LetvPaySdk.getInstance().quitSdk();
//                }
//
//                @Override
//                public void onSuccess(AccountInfo accountInfo) {
//                    // 乐视平台用户唯一标识id
//                    long userId = accountInfo.ssoUid;
//                    // 用户昵称
//                    String userName = accountInfo.username;
//                    // access_token
//                    String access_token = accountInfo.access_token;
//                    String msg = "登录成功,UserId：" + userId + ", UserName：" + userName + "," +
//                            "access_token" + access_token;
//                    loginByChannel(userName, "", userId + "");
//                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                    Log.e("result", msg);
//                }
//
//                @Override
//                public void onFailed(int errorCode, String errorMessage) {
//                    String msg = "登录失败, errorCode:" + errorCode + ", errorMessage:" +
//                            errorMessage;
//                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//                    Log.e("result", msg);
//                }
//            });
//            leshiLogin.login();
        } else if (channel.equals(TvChannelType.aliplay.name())) {
//            loginByChannel("fafdsa2faf", "", "fafafaf33addssfddasdfafdf");
//            aliPlayLoginImpl = new AliPlayLoginImpl(this);
//            aliPlayLoginImpl.setChannelLoginStateListener(new ChannelLoginStateListener() {
//                @Override
//                public void onSuccess(Listeners.UserInfo userInfo) {
//                    String nick = userInfo.getUserNick();
//                    String portrait = userInfo.getAvatarUrl();
//                    nick = (TextUtils.isEmpty(nick)) ? "" : nick;
//                    portrait = (TextUtils.isEmpty(portrait)) ? "" : portrait;
//                    loginByChannel(nick, portrait, userInfo.getUserId());
//                }
//
//                @Override
//                public void onFail() {
//                    Toast.makeText(ChannelLoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
//                }
//            });
//            aliPlayLoginImpl.login();
        } else if (channel.equals(TvChannelType.yishiteng.name())) {

        } else {
            Toast.makeText(ChannelLoginActivity.this, "未知渠道登录方式！", Toast.LENGTH_SHORT).show();
        }
//        permissionRequest();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_channel_login;
    }

    //渠道登录
    private void loginByChannel(final String nickName, final String portrait, final String
            openId) {
        LoginByChannelService service = new LoginByChannelService(this);
        service.setCallback(new IOpenApiDataServiceCallback<LoginByChannelResponse>() {
            @Override
            public void onGetData(LoginByChannelResponse data) {
                try {
                    //登录过
                    if (data.data.isLoginByQdSuccess == 1) {
                        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                                .USERINFO_LOGINVO, data.data.loginInfo);
                        Toast.makeText(ChannelLoginActivity.this, "登录成功！", Toast.LENGTH_SHORT)
                                .show();
                        LocalBroadcastManager.getInstance(ChannelLoginActivity.this)
                                .sendBroadcast(new Intent
                                        (UserFragment.USERINFO_UPDATE));
                        ChannelLoginActivity.this.finish();
                    }
                    //第一次登录
                    else {
                        bindWeixinDialog = new BindWeixinDialog(ChannelLoginActivity.this, new
                                BindWeixinDialog.OnLoginSuccessListener() {
                                    @Override
                                    public void onLoginSuccess() {
                                        bindQd(nickName, portrait, openId);
                                    }
                                }, data.data.cfg);
                        bindWeixinDialog.setOnCancelListener(new DialogInterface.OnCancelListener
                                () {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                registerByQd(nickName, portrait, openId);
                            }
                        });
                        bindWeixinDialog.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {

            }
        });
        StringBuilder builder = new StringBuilder();
        builder.append("openId=" + openId);
        builder.append("&nickname=" + nickName);
        builder.append("&portrait=" + portrait);
        service.post(builder.toString(), false);
    }

    /**
     * 用户第一次渠道登录取消了微信绑定，那就只能用渠道账号去注册
     */
    private void registerByQd(String nickName, String portrait, String openId) {
        RegisterByQdService service = new RegisterByQdService(this);
        service.setCallback(new IOpenApiDataServiceCallback<RegisterByQdResponse>() {
            @Override
            public void onGetData(RegisterByQdResponse data) {
                try {
                    loginSuccessAction(data.data.loginInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(ChannelLoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        StringBuilder builder = new StringBuilder();
        builder.append("openId=" + openId);
        builder.append("&nickname=" + nickName);
        builder.append("&portrait=" + portrait);
        service.post(builder.toString(), false);
    }

    /**
     * 登录成功操作
     */
    private void loginSuccessAction(LoginUserInfo userInfo) {
        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                .USERINFO_LOGINVO, userInfo);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent
                (UserFragment.USERINFO_UPDATE));
        ChannelLoginActivity.this.finish();
    }

    /**
     * 用户扫描了二维码，绑定微信
     *
     * @param nickName
     * @param portrait
     * @param openId
     */
    private void bindQd(String nickName, String portrait, String openId) {
        BindQdService service = new BindQdService(this);
        service.setCallback(new IOpenApiDataServiceCallback<CommonResponse>() {
            @Override
            public void onGetData(CommonResponse data) {
                Toast.makeText(ChannelLoginActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                ChannelLoginActivity.this.finish();
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(ChannelLoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        StringBuilder builder = new StringBuilder();
        builder.append("openId=" + openId);
        builder.append("&nickname=" + nickName);
        builder.append("&portrait=" + portrait);
        service.postAES(builder.toString(), false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (aliPlayLoginImpl != null) {
//            aliPlayLoginImpl.recycle();
//        }
//        LetvSdk.getLetvSDk().quit();
    }

    @Override
    public void onBackPressed() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(CoursePackDetailActivity
                .ON_LOGIN_FAIL));
        super.onBackPressed();
    }

    /**
     * Android6.0权限申请
     */
//    private void permissionRequest() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (ActivityCompat.checkSelfPermission(this,
//                    Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
//                    || ActivityCompat.checkSelfPermission(this,
//                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                    || ActivityCompat.checkSelfPermission(this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager
//                    .PERMISSION_GRANTED) {
//                // 申请权限
//                ActivityCompat.requestPermissions(this, DANGEROUS_PERMISSION,
//                        READ_PHONE_STATE_REQUEST_CODE);
//            } else {
//                // 权限已经授予,直接初始化
//                // LetvSdk.getLetvSDk().setDebug(true);
//                LetvSdk.getLetvSDk().init(getApplicationContext());
//            }
//        }
//    }
//
//    @TargetApi(23)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
//            grantResults) {
//        if (requestCode == READ_PHONE_STATE_REQUEST_CODE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // 权限授予成功,初始化
//                Log.i("result", "成功获得授权");
//                if (AppUitls.isDefaultProcess(getApplicationContext())) {
//                    // LetvSdk.getLetvSDk().setDebug(true);
//                    LetvSdk.getLetvSDk().init(getApplicationContext());
//                }
//            } else {
//                Log.i("result", "未获得授权");
//                // 三方处理自己逻辑,这里只做测试用
//                ToastUtil.showToast(this, "权限拒绝,自动退出");
//                finish();
//            }
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        LetvSdk.getLetvSDk().onActivityResult(requestCode, resultCode, data);
    }
}
