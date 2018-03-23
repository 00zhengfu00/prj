package com.lswuyou.tv.pm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.lswuyou.tv.pm.BaseApplication;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.common.Constant;
import com.lswuyou.tv.pm.fragment.UserFragment;
import com.lswuyou.tv.pm.log.AndroidLogger;
import com.lswuyou.tv.pm.log.Logger;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.response.account.LoginUserInfo;
import com.lswuyou.tv.pm.net.response.account.VerifyTokenResponse;
import com.lswuyou.tv.pm.net.response.account.WeixinLoginResponse;
import com.lswuyou.tv.pm.net.response.member.GetBuyQrcodeResponse;
import com.lswuyou.tv.pm.net.service.GetBuyQrcodeService;
import com.lswuyou.tv.pm.net.service.VerifyTokenService;
import com.lswuyou.tv.pm.net.service.WeixinLoginService;
import com.lswuyou.tv.pm.view.LoginDialog;
import com.lswuyou.tv.pm.view.TitleBarView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class UnLoginActivity extends BaseActivity {
    private TitleBarView mTitleBarView;
    private Logger logger = AndroidLogger.getLogger(LoginDialog.class);
    private Timer mTimer;
    private CountDownTimer countDownTimer;
    private String token;
    private int expireSeconds;
    private Handler handler = new Handler();
    private AsyncTask<String, Integer, Bitmap> genQrcodeBitmap;

    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView) findViewById(R.id.title);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_unlogin;
    }

    @Override
    protected void initView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
        mTitleBarView.setBtnLeft(0, R.string.login);
        getLoginUrl();
    }

    /**
     * 获取二维码登录Url->同时支持微信和app扫码登录
     */
    private void getLoginUrl() {
        GetBuyQrcodeService service = new GetBuyQrcodeService(this);
        service.setCallback(new IOpenApiDataServiceCallback<GetBuyQrcodeResponse>() {
            @Override
            public void onGetData(GetBuyQrcodeResponse data) {
                expireSeconds = data.data.cfg.expireSeconds;
                refreshUI(data.data.cfg.token);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {

            }
        });
        service.post("", false);
    }

    /**
     * 刷新UI
     *
     * @param token
     */
    private void refreshUI(String token) {
        this.token = token;
        String urlOrin = BaseApplication.getStartUpData().loginQrCodeUrl;
        String url = urlOrin + token;
        genQrcodeBitmap = new AsyncTask<String, Integer, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                if (isCancelled()) {
                    return null;
                }
                int size = getResources().getDimensionPixelSize(R.dimen.x300);
                return QRCodeEncoder.syncEncodeQRCode(params[0], size);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if (bitmap == null) {
                    return;
                }
                ImageView ivQrcode = (ImageView) findViewById(R.id.iv_qrCode);
                ivQrcode.setImageBitmap(bitmap);
                startCheckLoginState();
                startCountDown();
            }
        };
        genQrcodeBitmap.execute(url);
    }

    //微信登录与后台通信
    private void doWeinxinLogin(String code) {
        WeixinLoginService service = new WeixinLoginService(this);
        service.setCallback(new IOpenApiDataServiceCallback<WeixinLoginResponse>() {
            @Override
            public void onGetData(WeixinLoginResponse data) {
                try {
                    CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                            .USERINFO_LOGINVO, data.data.loginInfo);
                    Toast.makeText(UnLoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                    LocalBroadcastManager.getInstance(UnLoginActivity.this).sendBroadcast(new
                            Intent(UserFragment.USERINFO_UPDATE));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(UnLoginActivity.this, "登录异常！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(UnLoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        try {
            code = URLEncoder.encode(code, Constant.CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        service.post("code=" + code, true);
    }

    /**
     * 向服务端轮询登录状态
     */
    private void startCheckLoginState() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        doCheckLoginState();
                    }
                });
            }
        }, 5000, 2000);
    }

    /**
     * 向服务端查询登录结果
     */
    private void doCheckLoginState() {
        VerifyTokenService service = new VerifyTokenService(this);
        service.setCallback(new IOpenApiDataServiceCallback<VerifyTokenResponse>() {
            @Override
            public void onGetData(VerifyTokenResponse data) {
                try {
                    if (data.data.verifyTokenResultVo.status == 1) {
                        loginSuccessAction(data.data.verifyTokenResultVo.loginInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(UnLoginActivity.this, "登录异常！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(UnLoginActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("token=" + token, false);
    }

    /**
     * 登录成功操作
     */
    private void loginSuccessAction(LoginUserInfo userInfo) {
        BaseApplication.setLoginUserInfo(userInfo);
        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO, userInfo);
        mTimer.cancel();
        countDownTimer.cancel();
        Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(UserFragment.USERINFO_UPDATE));
//        startActivity(new Intent(UnLoginActivity.this, MainActivity.class));
        finish();
    }

    /**
     * 二维码有效期倒计时
     */
    private void startCountDown() {
        countDownTimer = new CountDownTimer(expireSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                expireSeconds = 0;
                mTimer.cancel();
                getLoginUrl();
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
