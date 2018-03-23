package com.lswuyou.tv.pm.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lswuyou.tv.pm.BaseApplication;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.fragment.UserFragment;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.response.account.LoginUserInfo;
import com.lswuyou.tv.pm.net.response.account.VerifyTokenResponse;
import com.lswuyou.tv.pm.net.response.member.GetBuyQrcodeResponse;
import com.lswuyou.tv.pm.net.service.GetBuyQrcodeService;
import com.lswuyou.tv.pm.net.service.VerifyTokenService;
import com.lswuyou.tv.pm.utils.Utils;
import com.lswuyou.tv.pm.view.TitleBarView;

import java.util.Timer;
import java.util.TimerTask;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * Created by huashigen on 2017-09-04.
 */

public class BuyMemberActivity extends BaseActivity {
    private TitleBarView mTitleBarView;
    private AsyncTask<String, Integer, Bitmap> genQrcodeBitmap;
    private int expireSeconds;
    private Timer mTimer;
    private CountDownTimer countDownTimer;
    private String token;
    private Handler handler = new Handler();

    @Override
    protected void findViewById() {
        mTitleBarView = (TitleBarView) findViewById(R.id.title);
    }

    @Override
    protected void initView() {
        mTitleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE);
        mTitleBarView.setBtnLeft(0, R.string.buyMember);
        TextView tvOneMonth = (TextView) findViewById(R.id.tv_one_month);
        TextView tvPriceOne = (TextView) findViewById(R.id.tv_price_one);
        final String nameOne = "1个月";
        String strOne = nameOne + "￥168";
        final SpannableStringBuilder spOneMonth = new SpannableStringBuilder(strOne);
        spOneMonth.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_money)), nameOne.length(), strOne.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
        tvOneMonth.setText(spOneMonth);
        tvPriceOne.setPaintFlags(Paint.ANTI_ALIAS_FLAG | Paint.STRIKE_THRU_TEXT_FLAG);
        TextView tvThreeMonth = (TextView) findViewById(R.id.tv_three_month);
        TextView tvPriceThree = (TextView) findViewById(R.id.tv_price_three);
        final String nameThree = "3个月";
        String strThree = nameThree + "￥468";
        final SpannableStringBuilder spThree = new SpannableStringBuilder(strThree);
        spThree.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_money)), nameThree.length(), strThree.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
        tvThreeMonth.setText(spThree);
        tvPriceThree.setPaintFlags(Paint.ANTI_ALIAS_FLAG | Paint.STRIKE_THRU_TEXT_FLAG);
        TextView tvSixMonth = (TextView) findViewById(R.id.tv_six_month);
        TextView tvPriceSix = (TextView) findViewById(R.id.tv_price_six);
        final String nameSix = "6个月";
        String strSix = nameSix + "￥888";
        final SpannableStringBuilder spSix = new SpannableStringBuilder(strSix);
        spSix.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_money)), nameSix.length(), strSix.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
        tvSixMonth.setText(spSix);
        tvPriceSix.setPaintFlags(Paint.ANTI_ALIAS_FLAG | Paint.STRIKE_THRU_TEXT_FLAG);
        TextView tvTwelveMonth = (TextView) findViewById(R.id.tv_twelve_month);
        TextView tvPriceTwelve = (TextView) findViewById(R.id.tv_price_twelve);
        final String nameTwelve = "12个月";
        String strTwelve = nameTwelve + "￥1678";
        final SpannableStringBuilder spTwelve = new SpannableStringBuilder(strTwelve);
        spTwelve.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_money)), nameTwelve.length(), strTwelve.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); //字体颜色
        tvTwelveMonth.setText(spTwelve);
        tvPriceTwelve.setPaintFlags(Paint.ANTI_ALIAS_FLAG | Paint.STRIKE_THRU_TEXT_FLAG);
        refreshUI();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_buy_member;
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
        String urlOrin = BaseApplication.getStartUpData().loginQrCodeUrlBuyMember;
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
                ImageView ivQrcode = (ImageView) findViewById(R.id.iv_qrcode);
                ivQrcode.setImageBitmap(bitmap);
                startCheckLoginState();
                startCountDown();
            }
        };
        genQrcodeBitmap.execute(url);
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
                    Toast.makeText(BuyMemberActivity.this, "登录异常！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                Toast.makeText(BuyMemberActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
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
//        startActivity(new Intent(BuyMemberActivity.this, MainActivity.class));
//        finish();
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

    private void refreshUI() {
        //+ "/" + Utils.getChannelId(this)
        String url = "";
        if (BaseApplication.getLoginUserInfo() != null) {
            url = BaseApplication.getLoginUserInfo().memberUrl;
            if (TextUtils.isEmpty(url)) {
                BaseApplication.setLoginUserInfo(null);
                CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.USERINFO_LOGINVO);
                CacheManager.remove(CacheManager.TYPE_USER_INFO, CacheKeys.PLAY_RECORD);
                LocalBroadcastManager.getInstance(BuyMemberActivity.this).sendBroadcast(new Intent(UserFragment.USERINFO_UPDATE));
                startActivity(new Intent(BuyMemberActivity.this, UnLoginActivity.class));
                return;
            }
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
                    ImageView ivQrcode = (ImageView) findViewById(R.id.iv_qrcode);
                    ivQrcode.setImageBitmap(bitmap);
                }
            };
            genQrcodeBitmap.execute(url);
        } else {
            getLoginUrl();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        genQrcodeBitmap.cancel(true);
    }
}
