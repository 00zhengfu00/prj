package com.lswuyou.tv.pm.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dangbei.euthenia.manager.DangbeiAdManager;
import com.dangbei.euthenia.manager.OnAdDisplayListener;
import com.dangbei.euthenia.ui.IAdContainer;
import com.lswuyou.tv.pm.AppConfigure;
import com.lswuyou.tv.pm.BaseApplication;
import com.lswuyou.tv.pm.R;
import com.lswuyou.tv.pm.common.CacheKeys;
import com.lswuyou.tv.pm.common.CacheManager;
import com.lswuyou.tv.pm.net.IOpenApiDataServiceCallback;
import com.lswuyou.tv.pm.net.response.account.AdsImgResponse;
import com.lswuyou.tv.pm.net.service.GetAdsImgService;
import com.lswuyou.tv.pm.view.AlertDialog;

public class SlashActivity extends BaseActivity {
    private ImageView ivAds;
    private long adsTime = 3000;
    private boolean allowBack = false;

    @Override
    protected void findViewById() {
        ivAds = (ImageView) findViewById(R.id.iv_ads);
    }

    @Override
    protected void initView() {
        getAdsImg();
//        IAdContainer adContainer = DangbeiAdManager.getInstance().createSplashAdContainer(this);
//        adContainer.setOnAdDisplayListener(new OnAdDisplayListener() {
//            @Override
//            public void onDisplaying() {
//
//            }
//
//            @Override
//            public void onFailed(Throwable throwable) {
//                start();
//            }
//
//            @Override
//            public void onFinished() {
//                start();
//            }
//
//            @Override
//            public void onClosed() {
//                start();
//            }
//
//            @Override
//            public void onTerminated() {
//                start();
//            }
//
//            @Override
//            public void onSkipped() {
//                start();
//            }
//
//            @Override
//            public void onTriggered() {
//                start();
//            }
//        });
//        adContainer.open();
    }

    private void start() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_slash;
    }

    private void getAdsImg() {
        if (!isNetworkOk()) {
            Toast.makeText(SlashActivity.this, "网络好像出问题了，请检查！", Toast.LENGTH_SHORT).show();
            allowBack = true;
            return;
        }
        GetAdsImgService service = new GetAdsImgService(this);
        service.setCallback(new IOpenApiDataServiceCallback<AdsImgResponse>() {
            @Override
            public void onGetData(AdsImgResponse data) {
                String imgUrl = data.data.startupImg;
                CacheManager.saveString(CacheManager.TYPE_USER_INFO, CacheKeys.RECOMMEND_QUALITY,
                        data.data.videoQuality);
                BaseApplication.setStartUpData(data.data);
                if (!TextUtils.isEmpty(imgUrl)) {
                    Glide.with(SlashActivity.this)
                            .load(Uri.parse(imgUrl))
                            .into(ivAds);
                } else {
                    adsTime = 0;
                }
                long timeDiff = System.currentTimeMillis() - data.data.currentTimeMillis;
                //时间差超过10分钟
                if (Math.abs(timeDiff) > 600000) {
                    AlertDialog dialog = new AlertDialog(SlashActivity.this,
                            "您的系统时间存在误差，可能会影响到本软件的使用，去设置？", new AlertDialog
                            .OnSubmitListener() {
                        @Override
                        public void onClick(boolean bool) {
                            if (bool) {
                                finish();
                                try {
                                    Intent intent = new Intent(Settings.ACTION_DATE_SETTINGS);
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                startDelay();
                            }
                        }
                    });
                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            startDelay();
                        }
                    });
                    dialog.show();
                } else {
                    startDelay();
                }
            }

            @Override
            public void onGetError(int errorCode, String errorMsg, Throwable error) {
                SlashActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                start();
                            }
                        }, 0);
                    }
                });
            }
        });
        String param = "";
        if (AppConfigure.huanwang == 1) {
            param = "hid=1";
        }
        service.post(param, false);
    }

    @Override
    public void onBackPressed() {
//        if (allowBack) {
        super.onBackPressed();
//        }
    }

    private void startDelay() {
        SlashActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        start();
                    }
                }, adsTime);
            }
        });
    }

    /**
     * 判断网络是否正常
     */
    private boolean isNetworkOk() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService
                (Context.CONNECTIVITY_SERVICE);
        if (null == connectivityManager) {
            return false;
        } else {
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
            if (null != infos && infos.length > 0) {
                for (NetworkInfo info : infos) {
                    if (NetworkInfo.State.CONNECTED == info.getState()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
