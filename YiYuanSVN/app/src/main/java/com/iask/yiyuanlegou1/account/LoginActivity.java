package com.iask.yiyuanlegou1.account;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iask.yiyuanlegou1.R;
import com.iask.yiyuanlegou1.base.BaseActivity;
import com.iask.yiyuanlegou1.base.BaseApplication;
import com.iask.yiyuanlegou1.common.cache.CacheKeys;
import com.iask.yiyuanlegou1.common.cache.CacheManager;
import com.iask.yiyuanlegou1.home.HomeActivity;
import com.iask.yiyuanlegou1.home.person.PersonFragment;
import com.iask.yiyuanlegou1.log.AndroidLogger;
import com.iask.yiyuanlegou1.log.Logger;
import com.iask.yiyuanlegou1.network.IOpenApiDataServiceCallback;
import com.iask.yiyuanlegou1.network.respose.account.LoginResponse;
import com.iask.yiyuanlegou1.network.service.account.LoginService;
import com.iask.yiyuanlegou1.utils.Util;
import com.iask.yiyuanlegou1.widget.TitleBarView;
import com.iask.yiyuanlegou1.wxapi.WXEntryActivity;

public class LoginActivity extends BaseActivity implements OnClickListener {
    private TitleBarView title;
    private TextView recoverPwdTv, registerTv;
    private Button loginBtn;
    private EditText loginU, loginP;
    private Logger logger = AndroidLogger.getLogger(RegisterActivity.class);
    private BroadcastReceiver mWinxinReceiver = null;

    @Override
    protected void findViewById() {
        title = (TitleBarView) findViewById(R.id.title);
        recoverPwdTv = (TextView) findViewById(R.id.forget_pwd);
        registerTv = (TextView) findViewById(R.id.go_regist_btn);
        loginBtn = (Button) findViewById(R.id.login_btn);
        loginU = (EditText) findViewById(R.id.login_u);
        loginP = (EditText) findViewById(R.id.login_p);
    }

    @Override
    protected void initView() {
        title.setCommonTitle(View.VISIBLE, View.VISIBLE);
        title.setBtnLeft(R.mipmap.back, R.string.whitespace);
        title.setBtnLeftOnclickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });
        title.setTitleText(R.string.login);
        recoverPwdTv.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        registerTv.setOnClickListener(this);
        findViewById(R.id.wxLoginBtn).setOnClickListener(this);
        if (!getPackageName().equals("com.iask.yiyuanlegou1")) {
            findViewById(R.id.wxLoginBtn).setVisibility(View.GONE);
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_pwd:
                startActivity(new Intent(LoginActivity.this,
                        RecoverPasswdActivity.class));
                break;
            case R.id.login_btn:
                doLogin();
                break;
            case R.id.go_regist_btn:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.wxLoginBtn:
                doWeixinLogin();
            default:
                break;
        }
    }

    private void doLogin() {
        String mobile = loginU.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            Toast.makeText(LoginActivity.this, "手机号码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        String pwd = loginP.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        LoginService service = new LoginService(this);
        service.setCallback(new IOpenApiDataServiceCallback<LoginResponse>() {
            @Override
            public void onGetData(LoginResponse data) {
                try {
                    Toast.makeText(LoginActivity.this, "登录成功~", Toast.LENGTH_SHORT).show();
                    CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                            .USERINFO_LOGINVO, data.data.user);
                    CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                            .OSS_SERVER_INFO, data.data.aliyun);
                    CacheManager.saveString(CacheManager.TYPE_PUBLIC, CacheKeys
                            .APPDATA_CURR_REGIONURL, data.data.aliyun.getRegionUrl());
                    String userId = data.data.user.getUserId() + "";
                    BaseApplication.getInstance().setUserId(userId);
                    hideInputSoft(LoginActivity.this, loginP);
                    LocalBroadcastManager manager = LocalBroadcastManager.getInstance
                            (LoginActivity.this);
                    manager.sendBroadcast(new Intent(PersonFragment.ACCOUNT_INFO_CHANGED));
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                logger.debug(errorMsg);
                Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("lgn=" + mobile + "&pwd=" + pwd, true);
    }

    public void doWeixinLogin() {
        if (Util.checkApkExist(this, "com.tencent.mm")) {
            // mDialog = ProgressDialog.show(this, "正在跳转", "请稍后...");
            // new Thread() {
            // public void run() {
            startWeixinAuthentication();
            // }
            // }.start();
        } else {
            Toast.makeText(this, "您还未安装微信，请先安装~", Toast.LENGTH_SHORT).show();
        }
    }

    private void startWeixinAuthentication() {
        // 将该app注册到微信
        Intent intent = new Intent(this, WXEntryActivity.class);
        intent.putExtra(WXEntryActivity.EXTRA_TYPE_USERCLICK, true);
        startActivity(intent);

        if (null == mWinxinReceiver) {
            mWinxinReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Intent intent1 = new Intent(PersonFragment.ACCOUNT_INFO_CHANGED);
                    LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(intent1);
                    LoginActivity.this.finish();
                }
            };
            registerReceiver(mWinxinReceiver, new IntentFilter(WXEntryActivity
                    .ACTION_WEIXIN_AUTHENCATION_SUCC));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWinxinReceiver != null) {
            unregisterReceiver(mWinxinReceiver);
        }
    }
}
