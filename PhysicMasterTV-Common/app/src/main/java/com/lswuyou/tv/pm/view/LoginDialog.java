package com.lswuyou.tv.pm.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.lswuyou.tv.pm.BaseApplication;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.channel.pay.TvChannelType;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.common.Constant;
import com.lswuyou.tv.pm.fragment.UserFragment;
import com.lswuyou.tv.pm.log.AndroidLogger;
import com.lswuyou.tv.pm.log.Logger;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.response.account.GetLoginCfgResponse;
import com.lswuyou.tv.pm.net.response.account.LoginCfgVo;
import com.lswuyou.tv.pm.net.response.account.LoginUserInfo;
import com.lswuyou.tv.pm.net.response.account.VerifyTokenResponse;
import com.lswuyou.tv.pm.net.response.account.WeixinLoginResponse;
import com.lswuyou.tv.pm.net.response.member.GetBuyQrcodeResponse;
import com.lswuyou.tv.pm.net.service.GetBuyQrcodeService;
import com.lswuyou.tv.pm.net.service.GetLoginCfgService;
import com.lswuyou.tv.pm.net.service.VerifyTokenService;
import com.lswuyou.tv.pm.net.service.WeixinLoginService;
import com.lswuyou.tv.pm.utils.UIUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Created by Administrator on 2016/8/15.
 */
public class LoginDialog extends Dialog implements DialogInterface.OnDismissListener {
    private Context mContext;
    private Logger logger = AndroidLogger.getLogger(LoginDialog.class);
    private OnLoginSuccessListener listener;
    private Timer mTimer;
    private String token;
    private int expireSeconds;
    private Handler handler = new Handler();
    private CountDownTimer countDownTimer;
    private LoginCfgVo loginCfgVo;
    private AsyncTask<String, Integer, Bitmap> genQrcodeBitmap;

    public LoginDialog(Context context, OnLoginSuccessListener listener, LoginCfgVo loginCfgVo) {
        super(context, R.style.CustomStyle);
        mContext = context;
        this.listener = listener;
        this.loginCfgVo = loginCfgVo;
        init();
    }

    public LoginDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }

    protected LoginDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_login);
        setOnDismissListener(this);
        getLoginUrl(loginCfgVo.cfg);
    }

    @Override
    public void show() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setAttributes(params);
        super.show();
    }

    /**
     * 获取微信二维码登录Url
     */
    private void getLoginUrl(Map<String, String> cfg) {
        GetBuyQrcodeService service = new GetBuyQrcodeService(getContext());
        service.setCallback(new IOpenApiDataServiceCallback<GetBuyQrcodeResponse>() {
            @Override
            public void onGetData(GetBuyQrcodeResponse data) {
                expireSeconds = data.data.cfg.expireSeconds;
                refreshUI(data.data.cfg.token);
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                UIUtils.showToast(getContext(), errorMsg);
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
                int size = getContext().getResources().getDimensionPixelSize(R.dimen.x300);
                return QRCodeEncoder.syncEncodeQRCode(params[0], size);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if (bitmap == null) {
                    return;
                }
                ImageView ivQrcode = (ImageView) findViewById(R.id.iv_qrcode);
                ivQrcode.setImageBitmap(bitmap);
                startCheckLoginState();
                startCountDown();
            }
        };
        genQrcodeBitmap.execute(url);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    //微信登录与后台通信
    private void doWeinxinLogin(String code) {
        WeixinLoginService service = new WeixinLoginService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<WeixinLoginResponse>() {
            @Override
            public void onGetData(WeixinLoginResponse data) {
                try {
                    loginSuccessAction(data.data.loginInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "登录异常！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        try {
            code = URLEncoder.encode(code, Constant.CHARACTER_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        service.post("code=" + code, true);
    }

    public interface OnLoginSuccessListener {
        public void onLoginSuccess();
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
        }, 10000, 2000);
    }

    /**
     * 向服务端查询登录结果
     */
    private void doCheckLoginState() {
        VerifyTokenService service = new VerifyTokenService(getContext());
        service.setCallback(new IOpenApiDataServiceCallback<VerifyTokenResponse>() {
            @Override
            public void onGetData(VerifyTokenResponse data) {
                try {
                    if (data.data.verifyTokenResultVo.status == 1) {
                        loginSuccessAction(data.data.verifyTokenResultVo.loginInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "登录异常！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
        service.post("token=" + token, false);
    }

    /**
     * 登录成功操作
     */
    private void loginSuccessAction(LoginUserInfo userInfo) {
        BaseApplication.setLoginUserInfo(userInfo);
        CacheManager.saveObject(CacheManager.TYPE_USER_INFO, CacheKeys
                .USERINFO_LOGINVO, userInfo);
        Toast.makeText(mContext, "登录成功！", Toast.LENGTH_SHORT).show();
        LoginDialog.this.dismiss();
        listener.onLoginSuccess();
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent
                (UserFragment.USERINFO_UPDATE));
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
                getLoginCfg();
            }
        };
        countDownTimer.start();
    }

    /**
     * 获取登录配置
     */
    private void getLoginCfg() {
        GetLoginCfgService service = new GetLoginCfgService(mContext);
        service.setCallback(new IOpenApiDataServiceCallback<GetLoginCfgResponse>() {
            @Override
            public void onGetData(GetLoginCfgResponse data) {
                try {
                    String loginType = data.data.loginCfgVo.loginType;
                    if (loginType.equals(TvChannelType.none.name())) {
                        getLoginUrl(data.data.loginCfgVo.cfg);
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "获取登录配置异常！", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(mContext, "获取登录配置信息失败！", Toast.LENGTH_SHORT).show();
            }
        });
        service.post("", true);
    }
}
